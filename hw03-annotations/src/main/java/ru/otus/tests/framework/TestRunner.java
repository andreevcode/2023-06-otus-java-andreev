package ru.otus.tests.framework;

public interface TestRunner {
    void runTestsFor(String className) throws ClassNotFoundException;
}
