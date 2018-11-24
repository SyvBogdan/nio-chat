package chat.server;

import chat.server.model.ChatServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerBoot {

    public static void main(String[] args) throws IOException {

        final String host = (args.length > 0) ? args[0] : "0.0.0.0";
        final int port = Integer.valueOf((args.length > 0) ? args[1] : "9999");
        final InetSocketAddress serverAddress = new InetSocketAddress(host, port);
        final ChatServer chatServer = new ChatServer(serverAddress);

        chatServer.startServer();
    }
}
