package chat.server.model;

import java.nio.channels.SelectionKey;

public interface IChatServer {

    void handleAcceptClientEvent(SelectionKey key);

    void handleRead(SelectionKey key);

    void handleWrite(SelectionKey key);

}
