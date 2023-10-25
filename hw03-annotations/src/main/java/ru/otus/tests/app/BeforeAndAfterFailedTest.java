package ru.otus.tests.app;

import ru.otus.tests.framework.After;
import ru.otus.tests.framework.Before;
import ru.otus.tests.framework.Test;

public class BeforeAndAfterFailedTest {

    @Before
    public void serviceASetUp() {
        System.out.println("\tServiceA preparation - OK");
    }

    @Before
    public void serviceBSetUp() {
        var message = "serviceB preparation - FAILED";
        System.out.printf("\tERROR - %s%n", message);
        throw new RuntimeException(message);
    }


    @Test
    public void FirstTest() {
    }

    @Test
    public void SecondTest() {
    }

    @Test
    public void ThirdTest() {
        throw new RuntimeException("Wrong data in test");
    }

    @After
    public void tearDownServiceA() {
        System.out.println("\tServiceA teardown - OK");
    }

    @After
    public void tearDownServiceB() {
        var message = "serviceB teardown - FAILED";
        System.out.printf("\tERROR - %s%n", message);
        throw new RuntimeException(message);
    }
}
