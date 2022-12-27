package de.zeus.server;

import de.zeus.server.netty.ClientChannelInitalizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;

/**
 * This class is the main class for the server. It will start the server and handle incoming connections.
 *
 * @author ZeusSeinGrossopa
 */
@ChannelHandler.Sharable
public class NettyServer {

    private NioEventLoopGroup nioEventLoopGroup;
    private ServerBootstrap bootstrap;
    private ChannelFuture channelFuture;

    private ArrayList<Channel> registeredChannels;
    private ArrayList<MessageReceived> messageReceivedListeners;

    public NettyServer() {}

    /**
     * Starts the server
     *
     * @param ip the host to bind to
     * @param port the port to bind to
     */
    public final void start(String ip, int port) throws RuntimeException {
        reset();

        System.out.println("Starting server on " + ip + ":" + port);
        bootstrap = new ServerBootstrap();
        bootstrap.group(nioEventLoopGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ClientChannelInitalizer(this));

        try {
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.AUTO_CLOSE, true)
                    .option(ChannelOption.SO_REUSEADDR, true);
        } catch(ChannelException ignored) {}

        try {
            channelFuture = bootstrap.localAddress(ip, port).bind().syncUninterruptibly();

            if(channelFuture.isSuccess())
                System.out.println("Server started successfully");
            else
                throw new RuntimeException("Unable to start server");
        } catch(Exception e) {
            System.err.println("Could not start server on " + ip + ":" + port);

            throw new RuntimeException("Unable to start server", e);
        }
    }

    /**
     * Stops the server
     */
    public final void stop() {
        for (Channel channel : getRegisteredChannels()) {
            if(channel != null && channel.isOpen()) {
                channel.close();
            }
        }

        if(nioEventLoopGroup != null)
            nioEventLoopGroup.shutdownGracefully();

        if(channelFuture != null)
            channelFuture.channel().close();

        reset();
    }

    /**
     * Resets all current variables.
     * <p>
     * Attention: Use this method only if the server is stopped!
     */
    public void reset() {
        nioEventLoopGroup = new NioEventLoopGroup(1);
        registeredChannels = new ArrayList<>();
        messageReceivedListeners = new ArrayList<>();
        bootstrap = null;
    }

    /**
     * Sends a message to all connected clients
     *
     * @param message the message to send
     */
    public void sendMessage(String message) {
        for (Channel channel : getRegisteredChannels()) {
            if(channel != null && channel.isOpen()) {
                channel.writeAndFlush(Unpooled.wrappedBuffer(message.getBytes()));
            }
        }
        System.out.println("Sent message: " + message);
    }

    /**
     * Sends a message to a specific client
     *
     * @param message the message to send
     * @param channel the channel to send the message to
     */
    public void sendMessageFor(String message, Channel channel) {
        if(channel != null && channel.isOpen()) {
            channel.writeAndFlush(Unpooled.wrappedBuffer(message.getBytes()));
        }
    }

    public NioEventLoopGroup getNioEventLoopGroup() {
        return nioEventLoopGroup;
    }

    public ServerBootstrap getBootstrap() {
        return bootstrap;
    }

    public ArrayList<Channel> getRegisteredChannels() {
        return registeredChannels;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void addMessageReceivedListener(MessageReceived messageReceivedListener) {
        messageReceivedListeners.add(messageReceivedListener);
    }

    public ArrayList<MessageReceived> getMessageReceivedListeners() {
        return messageReceivedListeners;
    }
}