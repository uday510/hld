package com.app.hld.cache.eviction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache<K, V> {

    private final int capacity;
    private final Map<K, Node<K, V>> cache;
    private final Node<K, V> head, tail;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public LRUCache(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public V get(K key) {
        lock.writeLock().lock();

        try {
            Node<K, V> node = cache.get(key);
            if (node == null) return null;

            moveToTail(node);
            return node.value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void put(K key, V value) {
        lock.writeLock().lock();

        try {
            Node<K, V> node = cache.get(key);

            if (node != null) {
                node.value = value;
                moveToTail(node);
            } else {
                if (cache.size() >= capacity) {
                    removeNode(head.next);
                }
                Node<K, V> newNode = new Node<>(key, value);
                addNode(newNode);
            }

        } finally {
            lock.writeLock().unlock();
        }
    }

    private void detach(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToTail(Node<K, V> node) {
        detach(node);
        addNode(node);
    }

    private void addNode(Node<K, V> node) {
        cache.put(node.key, node);

        Node<K, V> prev = tail.prev;

        prev.next = node;
        node.prev = prev;
        node.next = tail;
        tail.prev = node;
    }

    private void removeNode(Node<K, V> node) {
        if (node == head || node == tail) return;

        cache.remove(node.key);
        detach(node);

        node.prev = null;
        node.next = null;
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev, next;

        Node (K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
