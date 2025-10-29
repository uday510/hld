package com.app.hld.networking;

import java.io.*;
import java.net.*;


public class Client {

    public static void main(String[] args) {
        try {

            Socket socket = new Socket("localhost", 4000);
            System.out.println("Connected to server");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            
            output.println("Ping");

            String response = input.readLine();
            System.out.println("Server response: " + response);

            socket.close();
        } catch(IOException ignored) {}
    }
}