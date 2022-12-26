package de.zeus.client.netty;

import de.zeus.client.NettyClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class ChannelReader extends SimpleChannelInboundHandler<Object> {

    private final NettyClient nettyClient;

    public ChannelReader(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        nettyClient.disconnect();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channel, Object object) throws Exception {
        String message = ((ByteBuf) object).toString(Charset.defaultCharset());
        System.out.println("Received Message: " + message);

        nettyClient.getMessageReceivedListeners().forEach(listener -> listener.onMessageReceived(message));
    }
}
