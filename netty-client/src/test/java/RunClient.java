import de.zeus.client.MessageReceived;
import de.zeus.client.NettyClient;

public class RunClient {

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        nettyClient.connect("127.0.0.1", 8080);

        nettyClient.addMessageReceivedListener(new MessageReceived() {
            @Override
            public void onMessageReceived(String message) {
                System.out.println("Pong!");
            }
        });

        nettyClient.sendMessage("Hello World!");
    }
}