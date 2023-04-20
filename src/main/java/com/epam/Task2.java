package com.epam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Task2 {
    public static void main(String[] args) {
        Random random = new Random();
        Collection<Integer> numbers = new ArrayList<>();
        Thread writer = new Thread(() -> {
            while (true) {
                synchronized (numbers) {
                    numbers.add(random.nextInt(100));
                    Thread.yield();
                }
            }
        });
        Thread sumPrinter = new Thread(() -> {
            while (true) {
                synchronized (numbers) {
                    System.out.println("Sum of collection: " + numbers.stream().mapToInt(Integer::intValue).sum());
                    Thread.yield();
                }
            }
        });
        Thread sqrtSumPrinter = new Thread(() -> {
            while (true) {
                synchronized (numbers) {
                    System.out.println("Sum of square roots: " + numbers.stream().mapToDouble(Math::sqrt).sum());
                    Thread.yield();
                }
            }
        });

        writer.start();
        sumPrinter.start();
        sqrtSumPrinter.start();
        try {
            writer.join();
            sumPrinter.join();
            sqrtSumPrinter.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }
}
