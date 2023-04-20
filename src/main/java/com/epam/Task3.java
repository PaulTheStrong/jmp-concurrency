package com.epam;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class Task3 {

    public static void main(String[] args) {
        SyncQueue syncQueue = new SyncQueue();
        for (int i = 0; i < 5; i++) {
            new Thread(new Consumer(syncQueue, "Consumer " + i)).start();
            new Thread(new Producer(syncQueue, "Producer " + i)).start();
        }
    }

    private static class SyncQueue {
        private final Queue<String> messageQueue = new LinkedList<>();

        public synchronized void enqueue(String message) {
            messageQueue.add(message);
            notify();
        }

        public synchronized String pop() {
            while (messageQueue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return messageQueue.remove();
        }

    }

    private static class Consumer implements Runnable {

        private final SyncQueue syncQueue;
        private final String name;

        public Consumer(SyncQueue syncQueue, String name) {
            this.syncQueue = syncQueue;
            this.name = name;
        }

        public void consume() {
            String message = syncQueue.pop();
            System.out.println("Reading message from consumer " + name + " " + message);
        }

        @Override
        public void run() {
            while (true) {
                consume();
            }
        }
    }

    private static class Producer implements Runnable {

        private final SyncQueue syncQueue;
        private final String name;

        public Producer(SyncQueue syncQueue, String name) {
            this.syncQueue = syncQueue;
            this.name = name;
        }

        public void produce(String message) {
            syncQueue.enqueue(message);
            System.out.println("Writing message from producer " + name + " " + message);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            while (true) {
                produce(UUID.randomUUID().toString());
            }
        }
    }
}
