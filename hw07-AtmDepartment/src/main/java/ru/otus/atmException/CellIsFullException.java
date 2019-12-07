package ru.otus.atmException;

public class CellIsFullException extends AtmException {
    public CellIsFullException(String message) {
        super(message);
    }
}
