package com.app.hld.resilience;

import java.util.concurrent.atomic.AtomicInteger;

class DB {
    private AtomicInteger data = new AtomicInteger(10);

    public int read() {
        return data.get();
    }

    public void write(int newValue) {
        data.set(newValue);
    }
}

public class CAPDemo {

    public static void main(String[] args) {
        DB node1 = new DB();
        DB node2 = new DB();

        System.out.println("Initial Value: A=" + node1.read() + ", B=" + node2.read());

        System.out.println("Network partition detected!");

        System.out.println("\n--- Consistency (CP) ---");
        System.out.println("Node B refuses write to say consistent.");
        node1.write(20);
        System.out.println("A=" + node1.read() + ", B=" + node2.read());

        System.out.println("\nNetwork healed! Syncing nodes...");
        node2.write(node1.read());
        System.out.println("After sync: A=" + node1.read() + ", B=" + node2.read());
    }
}
