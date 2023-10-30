package ru.otus.autologging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LoggingAddCalculatorImplTest {
    private static Logger log = LoggerFactory.getLogger(LoggingAddCalculatorImplTest.class);

    private Calculator addCalculator;

    @BeforeEach
    public void setUp() {
        addCalculator = CalculatorLoggerWrapper.createLoggingAddCalculatorImpl(100);
    }

    @Test
    void calculateOneParamTest() {
        int param = 20;
        log.info("Starting calculateOneParamTest with param: {}", param);
        var result = addCalculator.calculate(param);
        assertThat(result).isEqualTo(120);
    }

    @Test
    void calculateTwoParamsTest() {
        int param1 = 20;
        int param2 = 10;
        log.info("Starting calculateTwoParamsTest with params: {}, {}", param1, param2);
        var result = addCalculator.calculate(param1, param2);
        assertThat(result).isEqualTo(130);
    }

    @Test
    void calculateThreeParamsTest() {
        int param1 = 20;
        int param2 = 10;
        int param3 = 7;
        log.info("Starting calculateThreeParamsTest with params: {}, {}, {}", param1, param2, param3);
        var result = addCalculator.calculate(param1, param2, param3);
        assertThat(result).isEqualTo(137);
    }
}
