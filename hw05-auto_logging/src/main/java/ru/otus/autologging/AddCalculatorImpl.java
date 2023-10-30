package ru.otus.autologging;

import ru.otus.autologging.annotation.Log;

public class AddCalculatorImpl implements Calculator{
    private final int baseValue;

    public AddCalculatorImpl(int baseValue) {
        this.baseValue = baseValue;
    }

    @Log
    @Override
    public int calculate(int param) {
        return baseValue + param;
    }

    @Log
    @Override
    public int calculate(int param1, int param2) {
        return baseValue + param1 + param2;
    }

    @Override
    public int calculate(int param1, int param2, int param3) {
        return baseValue + param1 + param2 + param3;
    }
}
