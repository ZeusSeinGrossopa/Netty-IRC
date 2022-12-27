package de.zeus.server;

import java.util.Scanner;

/**
 * This class is the entry point for the server if you want to run it from the command line.
 * It will start the server and wait for the user to type "stop" to stop the server.
 * Other lines will be sent to all connected clients.
 *
 * @author ZeusSeinGrossopa
 */
public class ExampleServer {

    public static NettyServer server;

    /**
     * The main method
     */
    public static void main(String[] args) {
        server = new NettyServer();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the port to start the server on: ");
        int port = scanner.nextInt();

        server.start("127.0.0.1", port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            System.out.println("Server stopped!");
        }));

        while (true) {
            String message = scanner.nextLine();
            if(message == null || message.isEmpty())
                continue;

            if(message.toLowerCase().startsWith("stop")) {
                server.stop();
                break;
            }
            server.sendMessage(message);
        }
    }
}