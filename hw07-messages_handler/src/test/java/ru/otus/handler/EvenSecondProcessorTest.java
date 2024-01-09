package ru.otus.handler;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Message;
import ru.otus.processor.EvenSecondException;
import ru.otus.processor.EvenSecondProcessor;
import ru.otus.processor.Processor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EvenSecondProcessorTest {
    private final static Logger log = LoggerFactory.getLogger(EvenSecondProcessorTest.class);
    private final Processor evenSecondProcessor = new EvenSecondProcessor(() -> LocalDateTime.now().getSecond());

    @Test
    @DisplayName("Тестируем выбрасывание ошибки на четной секунде")
    void evenSecondProcessorExceptionTest() throws InterruptedException {
        var message = new Message.Builder(1L).field11("field11").build();
        var second = LocalDateTime.now().getSecond();
        if (second % 2 != 0){
            Thread.sleep(1000);
            second = LocalDateTime.now().getSecond();
        }
        log.info("Current second: {}", second);

        // when
        var expectedEx = assertThrows(EvenSecondException.class, () -> evenSecondProcessor.process(message));

        // then
        var expectedExMsg = "Processor failed at even time second";
        assertEquals(expectedExMsg, expectedEx.getMessage());
    }

    @Test
    @DisplayName("Тестируем нормальную работу на нечетной секунде")
    void evenSecondProcessorNoExceptionTest() throws InterruptedException {
        var message = new Message.Builder(1L).field11("field11").build();
        var second = LocalDateTime.now().getSecond();
        if (second % 2 == 0){
            Thread.sleep(1000);
            second = LocalDateTime.now().getSecond();
        }
        log.info("Current second: {}", second);

        // when
        var result = evenSecondProcessor.process(message);

        // then
        assertEquals(message, result);
    }
}
