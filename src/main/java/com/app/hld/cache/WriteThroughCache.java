package com.app.hld.cache;

import java.util.HashMap;
import java.util.Map;

public class WriteThroughCache {

    private static final Map<Integer, String> cache = new HashMap<>();
    private static final Map<Integer, String> db = new HashMap<>();

    public static void write(int id, String value) {
        System.out.println("Writing through cache to DB...");
        cache.put(id, value);
        db.put(id, value);
    }

    public static String read(int id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }

        String data = db.get(id);
        if (data != null) cache.put(id, data);
        return data;
    }

    public static void main(String[] args) {
        write(1, "Alice");
        System.out.println(read(1));
    }
}
