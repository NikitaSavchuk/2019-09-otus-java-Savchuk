package ru.otus;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class Benchmark implements BenchmarkMBean {

    @Getter private int size = 0;
    private final int loopCounter;
    public List<Integer[]> list = new LinkedList<>();

    public Benchmark(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    void run() throws InterruptedException {
        while (true) {
            for (int i = 0; i < loopCounter; i++) {
                Demo.operationInMinute++;
                list.add(new Integer[480]);
                list.add(new Integer[480]);
                list.add(new Integer[480]);
                list.remove(0);
                list.remove(0);
            }
            Thread.sleep(10);
        }
    }

    @Override
    public void setSize(int size) {
        System.out.println("New size: " + size);
        this.size = size;
    }
}
