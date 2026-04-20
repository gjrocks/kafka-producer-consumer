package com.gj.kafka.employees;

import org.apache.kafka.streams.KafkaStreams;

import java.util.concurrent.CountDownLatch;

public class ThreadManager implements Runnable {
    private Thread thread;
    private String threadName;
    private KafkaStreams streams;
    CountDownLatch countDownLatch;
    ThreadManager(KafkaStreams streams, CountDownLatch latch) {
        this.streams = streams;
        threadName = "Thread-KafkaStreams-" + streams.toString();
        this.countDownLatch = latch;
        System.out.println("Creating " + threadName);
    }

    public void run() {
        System.out.println("Running " + threadName);
        try {
            Thread.sleep(30000);
            System.out.println("Shutting down stream in " + threadName);
            streams.close();
            countDownLatch.countDown();
        } catch (Exception e) {
            System.out.println("Exception in " + threadName + " : " + e.getMessage());
        }
    }
}
