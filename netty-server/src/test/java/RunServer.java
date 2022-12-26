import de.zeus.server.MessageReceived;
import de.zeus.server.NettyServer;

public class RunServer {

    public static void main(String[] args) {
        NettyServer server = new NettyServer();

        server.start("127.0.0.1", 8080);
        server.addMessageReceivedListener(new MessageReceived() {
            @Override
            public void onMessageReceived(String message) {
                server.sendMessage("Hello Client!");
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            System.out.println("Server stopped!");
        }));
    }
}