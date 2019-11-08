package ru.otus;

public class TestLogging implements TestInterface {
    @Log
    public void calculation(int param) {
    }

    public void calculation2(int param) {
        System.out.println("Without Log");
    }
}
