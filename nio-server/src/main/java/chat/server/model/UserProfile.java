package chat.server.model;

import chat.model.User;

import java.nio.channels.SocketChannel;

public class UserProfile {

    private User user;
    private SocketChannel userChannel;

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
}
