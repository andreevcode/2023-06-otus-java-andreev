package ru.otus.tests.app;

import ru.otus.tests.framework.After;
import ru.otus.tests.framework.Before;
import ru.otus.tests.framework.Test;

public class BaseTest {

    @Before
    public void serviceASetUp() {
        System.out.println("\tServiceA preparation - OK");
    }

    @Before
    public void serviceBSetUp() {
        System.out.println("\tServiceB preparation - OK");
    }


    @Test
    public void FirstTest() {
    }

    @Test
    public void SecondTest() {
    }

    @Test
    public void ThirdTest() {
        var message = "wrong data in test";
        System.out.printf("\tERROR - %s%n", message);
        throw new RuntimeException("wrong data in test");
    }

    @After
    public void tearDownServiceA() {
        System.out.println("\tServiceA teardown - OK");
    }

    @After
    public void tearDownServiceB() {
        System.out.println("\tServiceB teardown - OK");
    }
}
