package com.zhouguan.mylibrary2;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

public class DemoMap {
    @Test
    public void main() {
        ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("key1", 1);
        concurrentHashMap.put("key2", 2);
        concurrentHashMap.put("key3", 3);
        concurrentHashMap.put("key4", 4);
        concurrentHashMap.put("key5", 5);
        System.out.println(concurrentHashMap.get("key1"));
        System.out.println(concurrentHashMap.get("key6"));
        Integer put = concurrentHashMap.put("1", 1);
        System.out.println(put);

        System.out.println(concurrentHashMap.getOrDefault("key6", 0));
        /**
         * 1
         * * Output:
         * 0
         */
//        ExecutorService executor = Executors.newFixedThreadPool(2);

//        executor.submit(() -> {
//            for (int i = 0; i < 5; i++) {
//                concurrentHashMap.put("key" + i, i);
//                System.out.println(Thread.currentThread().getName() + " put: key" + i + " -> " + i);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        executor.submit(() -> {
//            for (int i = 0; i < 5; i++) {
//                Integer value = concurrentHashMap.get("key" + i);
//                System.out.println(Thread.currentThread().getName() + " get: key" + i + " -> " + value);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        executor.shutdown();
            }
}
