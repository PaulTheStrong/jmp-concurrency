package com.epam;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Task1 {

    public static void main(String[] args) {
        Map<Integer, Integer> map = Collections.synchronizedMap(new HashMap<>());
        Thread adder = new Thread(new ValueAdderHashMap(map, 100000));
        Thread sum = new Thread(new SumTask(map));

        adder.start();
        sum.start();

        try {
            adder.join();
            sum.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static class ValueAdderHashMap implements Runnable {

        private static final Random RANDOM = new Random();

        private final Map<Integer, Integer> valuesMap;
        private final Integer valueCount;

        public ValueAdderHashMap(Map<Integer, Integer> valuesMap, Integer valueCount) {
            this.valuesMap = valuesMap;
            this.valueCount = valueCount;
        }

        @Override
        public void run() {
            for (var i = 0; i < valueCount; i++) {
                valuesMap.put(i, i);
            }
        }
    }

    private static class SumTask implements Runnable {

        private final Map<Integer, Integer> valuesMap;

        public SumTask(Map<Integer, Integer> valuesMap) {
            this.valuesMap = valuesMap;
        }

        @Override
        public void run() {
            int sum = valuesMap.values().stream().mapToInt(value -> value).sum();
            System.out.println("Values sum: " + sum);
        }
    }

}
