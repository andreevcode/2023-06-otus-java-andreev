package ru.otus.tests.app;

import ru.otus.tests.framework.TestRunnerImpl;

public class TestFrameworkDemo {

    public static void main(String[] args) throws ClassNotFoundException {
        var baseTestRunner = new TestRunnerImpl();
        baseTestRunner.runTestsFor("ru.otus.tests.app.BaseTest");

        var beforeAndAfterFailedTestRunner = new TestRunnerImpl();
        beforeAndAfterFailedTestRunner.runTestsFor("ru.otus.tests.app.BeforeAndAfterFailedTest");
    }
}
