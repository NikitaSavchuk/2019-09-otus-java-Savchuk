package ru.otus.atmException;

public class CellOutOfAmountException extends AtmException {
    public CellOutOfAmountException(String message) {
        super(message);
    }
}
