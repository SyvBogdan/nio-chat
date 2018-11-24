package chat.server.util;

import java.net.InetSocketAddress;

public class ServerHelper {

    public static String getAddress(InetSocketAddress address) {
        final String host = address.getHostName();
        final int port = address.getPort();
        return String.format("%s:%s", host, port);
    }

}
