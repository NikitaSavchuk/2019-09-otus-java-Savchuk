package ru.otus;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

public class SequenceOfNumbers {
    //false - счет вперед, true - назад
    private boolean decrementFlag = false;
    private final int DECREMENT_START_VALUE = 9;

    private final Thread thread1;
    private final Thread thread2;

    private AtomicInteger counter1 = new AtomicInteger(1);
    private AtomicInteger counter2 = new AtomicInteger(1);

    public SequenceOfNumbers() {
        this.thread1 = new Thread(() -> action(counter1));
        this.thread2 = new Thread(() -> action(counter2));

        this.thread1.setName("Поток #1");
        this.thread2.setName("Поток #2");
    }

    public void start() {
        thread1.start();
        thread2.start();
    }

    private synchronized void action(AtomicInteger counter) {
        while (true) {
            //Если вышли за 10
            if (counter1.get() == 11 && counter2.get() == 11) {
                counter1.set(DECREMENT_START_VALUE);
                counter2.set(DECREMENT_START_VALUE);
                decrementFlag = true;
            }
            //начинаем считать вверх
            if (counter.get() == 1) {
                decrementFlag = false;
            }
            System.out.println(format("%s %d"
                    , Thread.currentThread().getName()
                    , decrementFlag ? counter.getAndDecrement() : counter.getAndIncrement()));
            waitFewSecond(1000);
            //будим потоки
            if (thread1.getState().equals(Thread.State.WAITING) || thread2.getState().equals(Thread.State.WAITING)) {
                notifyAll();
            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitFewSecond(int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
