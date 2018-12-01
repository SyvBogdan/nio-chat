package chat.client.util;

import chat.client.ChatClientBoot;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;

public class Util {

    public static Image getImage(String path) {
        try {
            final URL urlPath = ChatClientBoot.class.getClassLoader().getResource(path);
            return Toolkit.getDefaultToolkit().getImage(urlPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int findAvailablePort() {
        final int minPort = 1024;
        final int maxPort = 65535;

        final int portRange = maxPort - minPort;
        int candidatePort = minPort;

        do {
            ++candidatePort;
            if (candidatePort > portRange) {
                throw new IllegalStateException(String.format("Could not find an available port in the range [%d, %d] after %d attempts", 1024, 65535, candidatePort-minPort));
            }
        } while (!isPortAvailable(candidatePort));

        return candidatePort;
    }

    private static boolean isPortAvailable(int port) {
        try {
            final SocketChannel socketChannel = SocketChannel.open();
            socketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), port));
            socketChannel.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static InetSocketAddress getLocalAddress(final SocketChannel socketChannel){
        try {
           return (InetSocketAddress)socketChannel.getLocalAddress();
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }
}

