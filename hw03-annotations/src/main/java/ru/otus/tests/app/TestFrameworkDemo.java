package ru.otus.tests.app;

import ru.otus.tests.framework.TestRunnerImpl;

public class TestFrameworkDemo {

    public static void main(String[] args) {
        new TestRunnerImpl().runTestsFor("ru.otus.tests.app.StartTest");
        new TestRunnerImpl().runTestsFor("ru.otus.tests.app.BaseTest");
        new TestRunnerImpl().runTestsFor("ru.otus.tests.app.BeforeAndAfterFailedTest");
    }
}
