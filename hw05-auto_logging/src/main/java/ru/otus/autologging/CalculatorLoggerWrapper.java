package ru.otus.autologging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

        private final Map<String, Method> methods = new HashMap<>();

        public CalculatorInvocationHandler(Calculator calculator) {
            this.calculator = calculator;

            for (Method method : calculator.getClass().getDeclaredMethods()) {
                this.methods.put(method.getName() + Arrays.toString(method.getParameters()), method);
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            var methodSignature = method.getName() + Arrays.toString(method.getParameters());
            logIfNeeded(methodSignature, methods.get(methodSignature), args);
            return method.invoke(calculator, args);
        }

        private void logIfNeeded(String methodSignature, Method method, Object[] args) {
            if (method != null) {
                var needLog = Arrays
                        .stream(method.getAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().getName().equals(LOG));
                if (needLog) {
                    log.info("EXTRA LOGGING - invoking method: {}, parameters: {}",
                            methodSignature, Arrays.toString(args));
                }
            }
        }

        @Override
        public String toString() {
            return "CalculatorInvocationHandler{" + "calculator=" + calculator + '}';
        }
    }
}
