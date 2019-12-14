package ru.otus.api;

public class DbExecutorException extends RuntimeException {
    public DbExecutorException(String message) {
        super(message);
    }

    public DbExecutorException(Throwable cause) {
        super(cause);
    }
}
