package ru.otus.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.model.Message;
import ru.otus.processor.CurrentSecondProvider;
import ru.otus.processor.EvenSecondException;
import ru.otus.processor.EvenSecondProcessor;
import ru.otus.processor.Processor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EvenSecondProcessorTest {
    private final static int EVEN_SECOND = 40;
    private final static int ODD_SECOND = 41;

    private final CurrentSecondProvider evenSecondProviderMock = Mockito.mock(CurrentSecondProvider.class);
    private final Processor evenSecondProcessor = new EvenSecondProcessor(evenSecondProviderMock);

    @Test
    @DisplayName("Тестируем выбрасывание ошибки на четной секунде")
    void evenSecondProcessorExceptionTest() {
        // when
        Mockito.when(evenSecondProviderMock.getSecond()).thenReturn(EVEN_SECOND);
        var message = new Message.Builder(1L).field11("field11").build();
        var expectedEx = assertThrows(EvenSecondException.class, () -> evenSecondProcessor.process(message));

        // then
        var expectedExMsg = "Processor failed at even time second";
        assertEquals(expectedExMsg, expectedEx.getMessage());
    }

    @Test
    @DisplayName("Тестируем нормальную работу на нечетной секунде")
    void evenSecondProcessorNoExceptionTest() {
        // when
        Mockito.when(evenSecondProviderMock.getSecond()).thenReturn(ODD_SECOND);
        var message = new Message.Builder(1L).field11("field11").build();
        var result = evenSecondProcessor.process(message);

        // then
        assertEquals(message, result);
    }
}
