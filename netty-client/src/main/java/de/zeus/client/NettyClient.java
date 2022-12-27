package de.zeus.client;

import de.zeus.client.netty.ServerChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;

/**
 * This class is the main class of the client. It will start the client and handle the connection to the server.
 *
 * @author ZeusSeinGrossopa
 */
@ChannelHandler.Sharable
public class NettyClient {

    private Bootstrap bootstrap;
    private NioSocketChannel nioSocketChannel;
    private ChannelFuture channelFuture;
    private NioEventLoopGroup nioEventLoopGroup;

    private ArrayList<MessageReceived> messageReceivedListeners;

    public NettyClient() {
    }

    /**
     * Starts the client
     *
     * @param ip   the host to connect to
     * @param port the port to connect to
     */
    public final void connect(String ip, int port) {
        reset();

        System.out.println("Connecting to " + ip + ":" + port);

        bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ServerChannelInitializer(this));

        try {
            bootstrap.option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .option(ChannelOption.SO_KEEPALIVE, true);
        } catch (ChannelException ignored) {
        }

        try {
            channelFuture = bootstrap.connect(ip, port).syncUninterruptibly();

            if (channelFuture.isSuccess())
                System.out.println("Connected successfully");
            else
                throw new RuntimeException("Unable to connect");
        } catch (Exception e) {
            System.err.println("Could not connect to " + ip + ":" + port);

            throw new RuntimeException("Unable to connect to server", e);
        }
    }

    /**
     * Disconnects the client from the server
     */
    public final void disconnect() {
        if (nioSocketChannel != null && nioSocketChannel.isOpen())
            nioSocketChannel.close();

        if (channelFuture != null)
            channelFuture.channel().close();

        if (nioEventLoopGroup != null)
            nioEventLoopGroup.shutdownGracefully();

        reset();
    }

    /**
     * Sends a message to the server
     *
     * @param message the message to send
     */
    public void sendMessage(String message) {
        if (nioSocketChannel != null && nioSocketChannel.isOpen()) {
            nioSocketChannel.writeAndFlush(Unpooled.wrappedBuffer(message.getBytes())).addListener(future -> {
                if (future.isSuccess())
                    System.out.println("Sent message: " + message);
                else
                    System.err.println("Could not send message: " + message);
            });
        }
    }

    /**
     * Resets the client variables.
     *
     * <p>Should be called when the client is disconnected or when the client is started.</p>
     */
    public void reset() {
        bootstrap = new Bootstrap();
        nioSocketChannel = new NioSocketChannel();
        channelFuture = null;
        nioEventLoopGroup = new NioEventLoopGroup(1);
        messageReceivedListeners = new ArrayList<>();
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public NioSocketChannel getNioSocketChannel() {
        return nioSocketChannel;
    }

    public void setNioSocketChannel(NioSocketChannel nioSocketChannel) {
        this.nioSocketChannel = nioSocketChannel;
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