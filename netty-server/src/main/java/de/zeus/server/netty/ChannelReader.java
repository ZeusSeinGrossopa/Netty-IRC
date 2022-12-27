package de.zeus.server.netty;

import de.zeus.server.ExampleServer;
import de.zeus.server.NettyServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * This class is the channel reader for the server. It will read incoming messages and handle them to the {@link de.zeus.server.MessageReceived} listener.
 *
 * @author ZeusSeinGrossopa
 */
public class ChannelReader extends SimpleChannelInboundHandler<Object> {

    private final NettyServer nettyServer;

    public ChannelReader(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        nettyServer.getRegisteredChannels().remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channel, Object object) throws Exception {
        String message = ((ByteBuf) object).toString(Charset.defaultCharset());
        System.out.println("Received Message: " + message);

        nettyServer.getRegisteredChannels().stream().filter(e -> e != channel.channel()).forEach(e -> ExampleServer.server.sendMessageFor(message, e));
        nettyServer.getMessageReceivedListeners().forEach(listener -> listener.onMessageReceived(message));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (!cause.getMessage().equals("Connection reset"))
            super.exceptionCaught(ctx, cause);
    }
}