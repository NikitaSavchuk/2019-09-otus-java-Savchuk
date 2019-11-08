package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import static java.lang.String.format;

public class TestCalculator {
    private int a;
    private int b;
    private Calculator testClass;

    @Before
    public void beforeTest() {
        a = 1;
        b = 2;
        testClass = new Calculator();
    }

    @Test
    public void getSumTest() {
        int sum = testClass.getSum(a, b);
        System.out.println("Сумма: " + sum);
        if (sum != 3) throw new RuntimeException("error");
    }

    @Test
    public void getSumTest2() {
        int sum = testClass.getSum(a, b);
        System.out.println(format("Сумма с ошибков %s, а ожидали 0", sum));
        if (sum != 0) throw new RuntimeException("Исключение");
    }

    @Test
    public void getSubTest() {
        int sub = testClass.getSub(a, b);
        System.out.println("Вычитание: " + sub);
        if (sub != -1) throw new RuntimeException("error");
    }

    @After
    public void afterTest() {
        a = 0;
        b = 0;
    }
}
