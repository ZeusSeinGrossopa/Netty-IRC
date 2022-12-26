package de.zeus.server;

import java.util.Scanner;

public class ExampleServer {

    public static void main(String[] args) {
        NettyServer server = new NettyServer();
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