package ru.otus.processor;

import ru.otus.model.Message;

public class EvenSecondProcessor implements Processor{

    private final CurrentSecondProvider currentSecondProvider;

    public EvenSecondProcessor(CurrentSecondProvider currentSecondProvider) {
        this.currentSecondProvider = currentSecondProvider;
    }

    @Override
    public Message process(Message message) {
        if (currentSecondProvider.getSecond() % 2 == 0){
            throw new EvenSecondException("Processor failed at even time second");
        }
        return message;
    }
}
