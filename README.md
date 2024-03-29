# Netty Client and Server System

### Introduction

This is a simple **client and server system** that uses [Netty](https://netty.io/) to communicate between the two.
The client and server are both written in Java.

The server and client are command line applications or can be cloned and used for personal projects.

---

### Client

The server is a command line application that allows the user to send messages to the server.
The client can also receive messages from the server.

### Server

The server is a command line application that allows the user to send messages to all registered clients.
The server can also receive messages from the client.
Type the stop command for stopping the server.

--- 

## API

### Server

```java
    NettyServer server = new NettyServer();

    server.start("127.0.0.1", 8080);
    server.addMessageReceivedListener(new MessageReceived() {
        @Override
        public void onMessageReceived(String message) {
            System.out.println("Message from Client: " + message);
            server.sendMessage("Hello Client!");
        }
    });
```

### Client

```java
    NettyClient nettyClient = new NettyClient();
    nettyClient.connect("127.0.0.1", 8080);
    
    nettyClient.addMessageReceivedListener(new MessageReceived() {
        @Override
        public void onMessageReceived(String message) {
            System.out.println("Message from Server: " + message);
        }
    });
    
    nettyClient.sendMessage("Hello Server!");
```

---
