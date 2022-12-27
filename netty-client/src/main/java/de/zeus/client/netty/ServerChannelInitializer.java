package de.zeus.client.netty;

import de.zeus.client.NettyClient;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * This class is the channel initializer for the client. It will add the client handler to the pipeline.
 *
 * @author ZeusSeinGrossopa
 */
public class ServerChannelInitializer extends io.netty.channel.ChannelInitializer<NioSocketChannel> {

    private final NettyClient nettyClient;

    public ServerChannelInitializer(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    protected void initChannel(NioSocketChannel channel) throws Exception {
        nettyClient.setNioSocketChannel(channel);

        channel.pipeline()
                .addLast("handler", new ChannelReader(nettyClient));
    }

    public NettyClient getNettyClient() {
        return nettyClient;
    }
}
