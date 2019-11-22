package ru.otus.atmException;

public class CellIsFullException extends Exception {
    public CellIsFullException(String message) {
        super(message);
    }
}
