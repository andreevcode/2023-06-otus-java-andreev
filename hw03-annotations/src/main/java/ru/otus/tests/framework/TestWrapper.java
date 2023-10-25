package ru.otus.tests.framework;

import static ru.otus.tests.framework.ReflectionHelper.instantiate;

public class TestWrapper<T> {
    private final T testInstance;
    private final String testMethodName;

    public TestWrapper(Class<T> testClass, String testMethodName) {
        this.testInstance = instantiate(testClass);
        this.testMethodName = testMethodName;
    }

    public T getTestInstance() {
        return testInstance;
    }

    public String getTestMethodName() {
        return testMethodName;
    }
}
