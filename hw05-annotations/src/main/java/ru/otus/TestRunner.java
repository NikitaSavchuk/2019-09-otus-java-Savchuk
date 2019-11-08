package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static java.lang.String.format;

class TestRunner {

    private int succeed = 0;
    private int failed = 0;
    ArrayList<Method> beforeMethods;
    ArrayList<Method> testMethods;
    ArrayList<Method> afterMethods;

    public TestRunner() {
        beforeMethods = new ArrayList<>();
        testMethods = new ArrayList<>();
        afterMethods = new ArrayList<>();
    }

    public void run(Class<?> testClass) {

        Method[] methods = testClass.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getAnnotation(Before.class) != null) {
                beforeMethods.add(method);
                continue;
            }
            if (method.getAnnotation(Test.class) != null) {
                testMethods.add(method);
                continue;
            }
            if (method.getAnnotation(After.class) != null) {
                afterMethods.add(method);
                continue;
            }
        }

        if (testMethods.size() == 0) {
            System.out.println("Тесты отсутствуют!");
            return;
        }

        for (Method testMethod : testMethods) {
            try {
                Constructor<?> constructor = testClass.getConstructor(null);
                Object obj = constructor.newInstance(null);
                try {
                    if (!(beforeMethods.size() == 0)) {
                        for (Method beforeEachMethod : beforeMethods) {
                            beforeEachMethod.invoke(obj, null);
                        }
                    }
                    testMethod.invoke(obj, null);
                    succeed++;
                } catch (Exception e) {
                    failed++;
                    System.out.println("Ошибка во время теста " + testMethod.getName());
                } finally {
                    if (!(afterMethods.size() == 0)) {
                        try {
                            for (Method afterEachMethod : afterMethods) {
                                afterEachMethod.invoke(obj, null);
                            }
                        } catch (Exception e) {
                            System.out.println("Ошибка в методах после тестов!");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        System.out.println(format("Прошло успешно \"%s\", упало \"%s\", всего тестов \"%s\"", succeed, failed, testMethods.size()));
    }
}
