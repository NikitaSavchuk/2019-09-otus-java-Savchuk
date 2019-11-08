package ru.otus;

import java.lang.reflect.InvocationTargetException;

public class Demo {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        TestInterface demo = IoC.createMyClass(TestInterface.class, TestLogging.class);
        demo.calculation(6);
        demo.calculation2(6);
    }
}
