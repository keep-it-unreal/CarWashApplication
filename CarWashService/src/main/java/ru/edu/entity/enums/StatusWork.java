package ru.edu.entity.enums;

public enum StatusWork {
    NONE(0),
    PLANNED(1),
    COMPLETED(2),
    CANCEL_USER(3),
    CANCEL_OWNER(4);

    private final int value;

    StatusWork(int newValue) {
        value = newValue;
    }
    public int getValue() { return value; }
}
