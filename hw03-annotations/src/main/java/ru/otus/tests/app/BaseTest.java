package ru.otus.tests.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.tests.framework.After;
import ru.otus.tests.framework.Before;
import ru.otus.tests.framework.Test;

public class BaseTest {
    private static Logger log = LoggerFactory.getLogger(BaseTest.class);

    @Before
    public void serviceASetUp() {
        log.info("\tServiceA preparation - OK");
    }

    @Before
    public void serviceBSetUp() {
        log.info("\tServiceB preparation - OK");
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
        log.error("\t{}", message);
        throw new RuntimeException("wrong data in test");
    }

    @After
    public void tearDownServiceA() {
        log.info("\tServiceA teardown - OK");
    }

    @After
    public void tearDownServiceB() {
        log.info("\tServiceB teardown - OK");
    }
}
