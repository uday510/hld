package com.app.hld.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheAside {

    private static final Map<Integer, String> cache = new HashMap<>();

    public static String getUser(int id) {

        if (cache.containsKey(id)) {
            System.out.println("Cache hit!");
            return cache.get(id);
        }
        System.out.println("Cache miss!");
        String user = Database.getFromDB(id);
        cache.put(id, user);
        return user;
    }

    public static void main(String[] args) {
        int requestId = 1;
        System.out.println("requestId: " + requestId++ + " " + getUser(1));
        System.out.println("requestId: " + requestId + " " + getUser(1));
    }
}
