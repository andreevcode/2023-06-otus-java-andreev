package ru.otus.tests.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.tests.framework.After;
import ru.otus.tests.framework.Before;
import ru.otus.tests.framework.Test;

public class BeforeAndAfterFailedTest {
    private static Logger log = LoggerFactory.getLogger(BeforeAndAfterFailedTest.class);

    @Before
    public void serviceASetUp() {
        log.info("\tServiceA preparation - OK");
    }

    @Before
    public void serviceBSetUp() {
        var message = "serviceB preparation - FAILED";
        log.error("\t{}", message);
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
        log.info("\tServiceA teardown - OK");
    }

    @After
    public void tearDownServiceB() {
        var message = "serviceB teardown - FAILED";
        log.error("\t{}", message);
        throw new RuntimeException(message);
    }
}
