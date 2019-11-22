package ru.otus.atmException;

public class CellOutOfAmountException extends Exception {
    public CellOutOfAmountException(String message) {
        super(message);
    }
}
