package de.zeus.server.netty;

import de.zeus.server.NettyServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

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

        nettyServer.getMessageReceivedListeners().forEach(listener -> listener.onMessageReceived(message));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(!cause.getMessage().equals("Connection reset"))
            super.exceptionCaught(ctx, cause);
    }
}