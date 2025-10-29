import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {
        try {
            
            ServerSocket serverSocket = new ServerSocket(4000);
            System.out.println("Server started. Waiting for client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            String data = input.readLine();
            System.out.println("Received from client: " + data);

            output.println("Hello, " + data + "! (from server)");

            socket.close();
            serverSocket.close();
            System.out.println("Server closed.");
        } catch(IOException ignored) {}
    }
}