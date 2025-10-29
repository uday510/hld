
package com.app.hld.networking.rpc;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            PingImpl stud = (PingImpl) registry.lookup("Service1");

            String response = stud.testPing();
            System.out.println("Response from server: " + response);
        } catch (IOException ignored) {}
    }
}