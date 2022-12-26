package de.zeus.client;

import java.util.Scanner;

public class ExampleClient {

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

            if(message == null || message.isEmpty())
                continue;

            if(message.toLowerCase().startsWith("stop")) {
                nettyClient.disconnect();
                break;
            }
            nettyClient.sendMessage(message);
        }
    }

}