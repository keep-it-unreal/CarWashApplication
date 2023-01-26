package ru.edu.entity.enums;

public enum StatusFree {
    FREE(0),
    BUSY(1);

    private final int value;

    StatusFree(int newValue) {
        value = newValue;
    }
    public int getValue() { return value; }

}
