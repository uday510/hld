import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {
        try {
            PingImpl ping = new PingImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Service 1", ping);
            System.out.println("Server is ready..");
        } catch(IOException ignored) {}
    }
}