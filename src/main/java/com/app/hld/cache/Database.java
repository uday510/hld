package com.app.hld.cache;

import java.util.HashMap;
import java.util.Map;

public class Database {

    static final Map<Integer, String> db = new HashMap<>();

    static {
        db.put(1, "User: Alice");
        db.put(2, "User: Bob");
    }

    public static String getFromDB(int id) {
        System.out.println("Fetching from DB...");
        return db.get(id);
    }
}
