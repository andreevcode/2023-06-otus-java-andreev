package ru.otus.autologging;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.autologging.annotation.Log;

class CalculatorLoggerWrapper {

    private CalculatorLoggerWrapper() {
    }

    private static Logger log = LoggerFactory.getLogger(CalculatorLoggerWrapper.class);

    static Calculator createLoggingAddCalculatorImpl(int baseValue) {
        return (Calculator) Proxy.newProxyInstance(
                CalculatorLoggerWrapper.class.getClassLoader(),
                new Class<?>[]{Calculator.class},
                new CalculatorInvocationHandler(new AddCalculatorImpl(baseValue))
        );
    }

    static class CalculatorInvocationHandler implements InvocationHandler {
        private static final String LOG = Log.class.getName();
        private final Calculator calculator;
        private final Set<String> methodSignatures;

        private String getMethodSignature(Method method) {
            return method.getName() + Arrays.toString(method.getParameters());
        }

        private void logIfNeeded(Method method, Object[] args) {
            var methodSignature = getMethodSignature(method);
            if (this.methodSignatures.contains(methodSignature)) {
                log.info("EXTRA LOGGING - invoking method: {}, parameters: {}",
                        methodSignature, Arrays.toString(args));
            }
        }

        private boolean hasLogAnnotation(Annotation annotation) {
            return annotation.annotationType().getName().equals(LOG);
        }

        public CalculatorInvocationHandler(Calculator calculator) {
            this.calculator = calculator;

            this.methodSignatures = Arrays.stream(calculator.getClass().getDeclaredMethods())
                    .filter(method -> Arrays.stream(method.getAnnotations())
                            .anyMatch(this::hasLogAnnotation))
                    .map(this::getMethodSignature)
                    .collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            logIfNeeded(method, args);
            return method.invoke(calculator, args);
        }

        @Override
        public String toString() {
            return "CalculatorInvocationHandler{" + "calculator=" + calculator + '}';
        }
    }
}
