package com.app.hld.consistent_hashing;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<String> servers = Arrays.asList("Server1", "Server2", "Server3");

        ConsistentHashing<String> consistentHashing = new ConsistentHashing<>(100, servers);

        String[] users = {"A", "B", "C", "D", "E"};

        consistentHashing.logRing();

        System.out.println("--- Intial Rounting ---");
        for (String user : users) {
            System.out.println(user + " is routed to: " + consistentHashing.getNode(user));
        }

        System.out.println("\n--- Server2 Crashes ---");
        consistentHashing.removeNode("Server2");

        for (String user : users) {
            System.out.println(user + " is routed to: " + consistentHashing.getNode(user));
        }
        
    }

}