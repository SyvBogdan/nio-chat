package chat.model;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {

    private static final long serialVersionUID = -2098510785894314005L;

    private String userName;
    private InetSocketAddress socketAddress;

    private boolean isConnected;

    private transient List<String> userChatHistory = new LinkedList<>();

    public User(String userName) {
        this.userName = userName;
    }

    public User(String userName, InetSocketAddress socketAddress) {
        this.userName = userName;
        this.socketAddress = socketAddress;
    }

    public void putNewMessageToHistory(String message) {
        userChatHistory.add(message);
    }

    public List<String> getUserChatHistory() {
        return userChatHistory;
    }

    public void setUserChatHistory(List<String> userChatHistory) {
        this.userChatHistory = userChatHistory;
    }

    public String getIp() {
        return socketAddress.getHostName();
    }

    public int getPort() {
        return socketAddress.getPort();
    }

    public String getUserName() {
        return userName;
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) &&
                Objects.equals(socketAddress, user.socketAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, socketAddress);
    }


    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
