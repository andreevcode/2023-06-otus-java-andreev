package ru.otus.tests.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ru.otus.tests.framework.ReflectionHelper.callMethod;

public class TestRunnerImpl implements TestRunner {
    private static final int OK_RESULT = 1;
    private static final int FAILED_RESULT = 0;
    private static final String BEFORE = Before.class.getName();
    private static final String AFTER = After.class.getName();
    private static final String TEST = Test.class.getName();
    private static final String OK = "OK";
    private static final String FAILED = "FAILED";

    private final Logger log = LoggerFactory.getLogger(TestRunnerImpl.class);
    private final List<String> testMethods = new ArrayList<>();
    private final List<String> beforeMethods = new ArrayList<>();
    private final List<String> afterMethods = new ArrayList<>();
    private Class<?> testClass;

    @Override
    public void runTestsFor(String className) {
        try {
            testClassLookup(className);
            log.info("\n::::::::::::::::::::: Starting tests for {}\n", testClass.getName());
            printSummary(runTests(), testMethods.size());
        } catch (ClassNotFoundException e) {
            log.error("Can't find class {} {}", className, e);
        }
    }

    private void testClassLookup(String className) throws ClassNotFoundException {
        testClass = Class.forName(className);
        var methods = Arrays.asList(testClass.getMethods());
        methods.forEach(method -> {
            for (var annotation : method.getAnnotations()) {
                var annotationName = annotation.annotationType().getName();
                var methodName = method.getName();
                if (annotationName.equals(BEFORE)) {
                    beforeMethods.add(methodName);
                } else if (annotationName.equals(TEST)) {
                    testMethods.add(methodName);
                } else if (annotationName.equals(AFTER)) {
                    afterMethods.add(methodName);
                }
            }
        });
    }

    private void printSummary(int good, int all) {
        log.info("-------------------\nTESTS COUNT: {}\nGOOD: {}\nBAD: {}\n", all, good, all - good);
    }

    private int runTests() {
        int finishedGood = 0;
        return testMethods.stream()
                .map(testMethod -> new TestWrapper<>(testClass, testMethod))
                .map(this::runTestFor)
                .reduce(finishedGood, Integer::sum);
    }

    private <T> int runTestFor(TestWrapper<T> test) {
        int result = 0;
        if (preparedFor(test)) {
            result = runTest(test, res -> log.info("TEST {}: result {}", test.getTestMethodName(), res));
        }
        tearDownAfter(test);
        return result;
    }

    private String resolveResult(boolean result) {
        return result ? OK : FAILED;
    }

    private <T> boolean preparedFor(TestWrapper<T> test) {
        log.info("TEST {}: preparation started......", test.getTestMethodName());
        var result = beforeMethods.stream().allMatch(before -> getMethodResult(test, before));
        log.info("TEST {}: preparation {}", test.getTestMethodName(), resolveResult(result));
        return result;
    }

    private <T> void tearDownAfter(TestWrapper<T> test) {
        log.info("TEST {}: teardown started......", test.getTestMethodName());
        var result = afterMethods.stream().allMatch(after -> getMethodResult(test, after));
        log.info("TEST {}: teardown {}\n--------------", test.getTestMethodName(), resolveResult(result));
    }

    private <T> int runTest(TestWrapper<T> test, Consumer<String> callback) {
        log.info("TEST {}: test started......", test.getTestMethodName());
        var result = getMethodResult(test, test.getTestMethodName());
        callback.accept(resolveResult(result));
        return result ? OK_RESULT : FAILED_RESULT;
    }

    private <T> boolean getMethodResult(TestWrapper<T> test, String method) {
        try {
            callMethod(test.getTestInstance(), method);
        } catch (RuntimeException e) {
            log.error("Error during test run for {}", method, e);
            return false;
        }
        return true;
    }
}
