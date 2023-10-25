package ru.otus.tests.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static ru.otus.tests.framework.ReflectionHelper.callMethod;

public class TestRunnerImpl implements TestRunner {
    private static final String BEFORE = Before.class.getName();
    private static final String AFTER = After.class.getName();
    private static final String TEST = Test.class.getName();
    private static final String OK = "OK";
    private static final String FAILED = "FAILED";
    private final List<String> testMethods = new ArrayList<>();
    private final List<String> beforeMethods = new ArrayList<>();
    private final List<String> afterMethods = new ArrayList<>();
    private Class<?> testClass;

    @Override
    public void runTestsFor(String className) throws ClassNotFoundException {
        testClassLookup(className);
        System.out.printf("::::::::::::::::::::: Starting tests for %s%n%n", testClass.getName());
        printSummary(runTests(), testMethods.size());
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
        System.out.printf("-------------------%nTESTS COUNT: %d%nGOOD: %d%nBAD: %d%n%n%n", all, good, all - good);
    }

    private int runTests() {
        int finishedGood = 0;
        return testMethods.stream()
                .map(this::runTestFor)
                .reduce(finishedGood, Integer::sum);
    }

    private int runTestFor(String testMethod) {
        boolean result = false;
        var test = new TestWrapper<>(testClass, testMethod);

        if (preparedFor(test)) {
            result = runTest(test, res -> System.out.printf("TEST %s: result %s%n", testMethod, res));
        }
        tearDownAfter(test);

        return result ? 1 : 0;
    }

    private String resolveResult(boolean result) {
        return result ? OK : FAILED;
    }

    private <T> boolean preparedFor(TestWrapper<T> test) {
        System.out.printf("TEST %s: preparation started......%n", test.getTestMethodName());
        var result = beforeMethods.stream().allMatch(before -> getMethodResult(test, before));
        System.out.printf("TEST %s: preparation %s%n", test.getTestMethodName(), resolveResult(result));
        return result;
    }

    private <T> void tearDownAfter(TestWrapper<T> test) {
        System.out.printf("TEST %s: teardown started......%n", test.getTestMethodName());
        var result = afterMethods.stream().allMatch(after -> getMethodResult(test, after));
        System.out.printf("TEST %s: teardown %s%n--------------%n", test.getTestMethodName(), resolveResult(result));
    }

    private <T> boolean runTest(TestWrapper<T> test, Consumer<String> callback) {
        System.out.printf("TEST %s: test started......%n", test.getTestMethodName());
        var result = getMethodResult(test, test.getTestMethodName());
        callback.accept(resolveResult(result));
        return result;
    }

    private <T> boolean getMethodResult(TestWrapper<T> test, String method) {
        try {
            callMethod(test.getTestInstance(), method);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
