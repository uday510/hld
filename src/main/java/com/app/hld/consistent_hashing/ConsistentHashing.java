package com.app.hld.consistent_hashing;

import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class ConsistentHashing<T> {

    private final HashFunction hashFunction;

    private final int numberOfReplicas;

    private final SortedMap<Long, T> circle = new TreeMap<>();

    public ConsistentHashing(int numberOfReplicas, Collection<T> nodes) {
        this.hashFunction = new MD5HashFunction();
        this.numberOfReplicas = numberOfReplicas;

        for (T node : nodes) {
            addNode(node);
        }

    }

    private void addNode(T node) {

        for (int i = 0; i < numberOfReplicas; i++) {

            long hash = hashFunction.hash(node.toString() + "-" + i);
            circle.put(hash, node);
        }

    }

    public void removeNode(T node) {
        
        for (int i = 0; i < numberOfReplicas; i++) {

            long hash = hashFunction.hash(node.toString() + "-" + i);
            circle.remove(hash);
        }
    }

    public T getNode(String key) {
        if (circle.isEmpty()) {
            return null;
        }


        long hash = hashFunction.hash(key);

        if (!circle.containsKey(hash)) {

            SortedMap<Long, T> tailMap = circle.tailMap(hash);

            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }

        return circle.get(hash);
    }

    public void logRing() {
        System.out.println("\n=== Hash Ring Status ===");
        System.out.println("Total Virtual Nodes on Ring: " + circle.size());
        
        for (Map.Entry<Long, T> entry : circle.entrySet()) {
            System.out.println("Hash: " + entry.getKey() + "  -->  Node: " + entry.getValue());
        }
        System.out.println("========================\n");
    }


}