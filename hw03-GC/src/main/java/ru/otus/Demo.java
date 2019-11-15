package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;
import javax.swing.Timer;

import static java.lang.String.format;

/*
VM options:
-Xms512m
-Xmx512m
-XX:+UseConcMarkSweepGC || -XX:+UseSerialGC || -XX:+UseParallelGC || -XX:+UseG1GC
 */

public class Demo {
    private static int youngCountInMinute;
    private static long youngDuration;
    private static int allYoungCountInMinute;
    private static long allYoungDuration;

    private static int oldCountInMinute;
    private static long oldDuration;
    private static int allOldCountInMinute;
    private static long allOldDuration;

    private static int currentMinute = 1;

    static int operationInMinute = 0;

    public static void main(String[] args) throws NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, MalformedObjectNameException {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchOnMonitoring();
        long beginTime = System.currentTimeMillis();

        int size = 5 * 1000;
        int loopCounter = 10;

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");
        Benchmark mbean = new Benchmark(loopCounter);
        mbs.registerMBean(mbean, name);

        mbean.setSize(size);

        Timer timer = new Timer(60000, x -> {
            System.out.printf("Прошло %s минуты... " +
                            "Количество young сборок: %d (Продолжительность %d ms)" +
                            ", количество old сборок: %d (Продолжительность %d ms)\n",
                    currentMinute,
                    youngCountInMinute,
                    youngDuration,
                    oldCountInMinute,
                    oldDuration);

            System.out.println("Количество операций в минуту: " + operationInMinute);

            allStatistic(youngCountInMinute, youngDuration, oldCountInMinute, oldDuration);
            System.out.println(format("Количество всех young сборок: %s (Продолжительность %s ms)" +
                            ", количество всех old сборок: %s (Продолжительность %d ms)"
                    , allYoungCountInMinute
                    , allYoungDuration
                    , allOldCountInMinute
                    , allOldDuration));

            resetStatistic();
        });

        timer.start();

        try {
            mbean.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("time:" + (System.currentTimeMillis() - beginTime) / 1000);
    }

    private static void allStatistic(int youngCountInMinute, long youngDuration, int oldCountInMinute, long oldDuration) {
        allYoungCountInMinute += youngCountInMinute;
        allYoungDuration += youngDuration;
        allOldCountInMinute += oldCountInMinute;
        allOldDuration += oldDuration;
    }

    private static void resetStatistic() {
        operationInMinute = 0;

        currentMinute++;
        youngCountInMinute = 0;
        youngDuration = 0;
        oldCountInMinute = 0;
        oldDuration = 0;
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    if (gcAction.equals("end of minor GC")) {
                        youngCountInMinute++;
                        youngDuration += duration;
                    }
                    if (gcAction.equals("end of major GC")) {
                        oldCountInMinute++;
                        oldDuration += duration;
                    }

                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }
}
