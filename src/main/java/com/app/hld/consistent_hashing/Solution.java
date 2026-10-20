package com.app.hld.consistent_hashing;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Solution {

    public static void main(String[] args) {

        TreeMap<Integer, String> map = new TreeMap<>();

        for (int i = 1; i <= 10; i += 2) {
            map.put(i, i + "");
        }

        System.out.println(map);
        System.out.println(map.ceilingKey(2));
        System.out.println(map.floorEntry(2));
    }
}
