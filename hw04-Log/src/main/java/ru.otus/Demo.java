package ru.otus;

public class Demo {
    public static void main(String[] args) {
        TestInterface demo = IoC.createMyClass();
        demo.calculation(6);
        demo.calculation2(6);
    }
}
