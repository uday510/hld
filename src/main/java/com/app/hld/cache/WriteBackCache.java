package com.app.hld.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class WriteBackCache {

    private static final Map<Integer, String> cache = new HashMap<>();
    private static final Map<Integer, String> db = new HashMap<>();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void write(int id, String value) throws ExecutionException, InterruptedException {
        cache.put(id, value);
        System.out.println("Write to cache, async DB update...");

        executor.submit(() -> {
           try {
               Thread.sleep(1000);
               db.put(id, value);
               System.out.println("DB updated asynchronously for ID: " + id);
           } catch (InterruptedException ignored) {}
        });
    }

    public static String read(int id) {
        if (cache.containsKey(id)) {
            System.out.println("Cache hit!");
            return cache.get(id);
        }

        System.out.println("Cache miss. Loading from DB...");
        String value = db.get(id);
        if (value != null) cache.put(id, value);
        return value;
    }

    public static void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ignored) {}
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        write(1, "Alice");
        write(2, "Bob");

        System.out.println("Request completed immediately!");
        Thread.sleep(1000);

        System.out.println("Reading data:");
        System.out.println("User 1: " + read(1));
        System.out.println("User 2: " + read(2));

        shutdown();
    }
}
