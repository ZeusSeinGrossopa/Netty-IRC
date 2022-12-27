package de.zeus.client;

import java.util.Scanner;

/**
 * This class is the entry point for the client if you want to run it from the command line.
 * It will start the client and wait for the user to type "stop" to stop the client.
 * Other lines will be sent to the server.
 *
 * @author ZeusSeinGrossopa
 */
public class ExampleClient {

    /**
     * The main method
     */
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the server ip: ");
        String ip = scanner.nextLine();

        System.out.println("Enter the server port: ");
        int port = scanner.nextInt();

        nettyClient.connect(ip, port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            nettyClient.disconnect();
            System.out.println("Client disconnected!");
        }));

        while (true) {
            String message = scanner.nextLine();

            if (message == null || message.isEmpty())
                continue;

            if (message.toLowerCase().startsWith("stop")) {
                nettyClient.disconnect();
                break;
            }
            nettyClient.sendMessage(message);
        }
    }
}