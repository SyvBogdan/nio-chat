package chat.server.model;

import chat.model.User;

import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserProfile {

    private User user;
    private SocketChannel userChannel;
    private Queue<Object> pendingForSend = new ArrayDeque<>();
    private final AtomicBoolean readyForNextSend = new AtomicBoolean(true);

    public UserProfile(User user, SocketChannel userChannel) {
        this.user = user;
        this.userChannel = userChannel;
    }

    public User getUser() {
        return user;
    }

    public SocketChannel getUserChannel() {
        return userChannel;
    }

    public Queue<Object> getPendingForSend() {
        return pendingForSend;
    }

    public AtomicBoolean getReadyForNextSend() {
        return readyForNextSend;
    }
}
