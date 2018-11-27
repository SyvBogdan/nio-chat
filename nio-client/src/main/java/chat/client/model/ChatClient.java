package chat.client.model;


import chat.model.Message;
import chat.model.User;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static chat.client.util.Util.findAvailablePort;


public class ChatClient {

    private static Object obj = new Object();

    private User myUser;

    private User activeUser;

    public User getLocalUser() {
        return myUser;
    }

    private DefaultListModel<String> graphicList;

    private Selector selector;

    private SocketChannel socketChannel;

    private ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<>();

    private MessageWriter messageWriter;

    private TextArea outPutTextArea;

    private List<String> generalInfo = new LinkedList<>();

    public boolean startClient(final String userName) {

        try {
            final SocketChannel socketChannel = openConnection();
            this.socketChannel = socketChannel;
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

            myUser = new User(userName, (InetSocketAddress) socketChannel.getLocalAddress());
            messageWriter = new MessageWriter(socketChannel);

            //fire register me on the server
            messageWriter.writeToServer(myUser);

            new Thread(this::startServerListening).start();

            return true;

        } catch (Throwable e) {
            generalInfo.add(e.toString());
            if (activeUser == null) outPutTextArea.append(e.toString());
            return false;
        }
    }

    private void startServerListening() {

        try {

            while (!Thread.interrupted()) {
                final int readyChannels = selector.selectNow();
                if (readyChannels == 0) {
                    continue;
                }
                final Set<SelectionKey> keySet = selector.selectedKeys();
                final Iterator<SelectionKey> keyIterator = keySet.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey currentKey = keyIterator.next();
                    keyIterator.remove();

                    if (!currentKey.isValid()) {
                        continue;
                    }
                    if (currentKey.isConnectable()) {
                        System.out.println("I'm connected to the server!");
                        handleConnectable(currentKey);
                    }
                    if (currentKey.isReadable()) {
                        handleRead(currentKey);
                    }

                    if (currentKey.isWritable()) {
                        currentKey.interestOps(SelectionKey.OP_READ);
                    }

                }
            }
        } catch (Exception e) {
            generalInfo.add(e.toString());
            if (activeUser == null) outPutTextArea.append("Eception in nestedloop " + e);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleRead(final SelectionKey key) throws IOException {

        final SocketChannel socketChannel = (SocketChannel) key.channel();

        Object message = null;

        final ByteBuffer buf = ByteBuffer.allocate(10000);

        try {
            socketChannel.read(buf);
            buf.flip();

            final byte[] arrByte = new byte[buf.limit()];
            buf.get(arrByte);

            ByteArrayInputStream in = new ByteArrayInputStream(arrByte);
            ObjectInputStream is = new ObjectInputStream(in);
            message = is.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if (message instanceof User) {

            User user = null;
            try {

                user = (User) message;
                if (user.isConnected()) {

                    if (user.equals(myUser)) {
                        final String status = "I am connected :)\n";
                        generalInfo.add(status);
                        if (activeUser == null) outPutTextArea.append(status);
                    } else {
                        graphicList.add(graphicList.getSize(), user.getUserName());
                        user.setUserChatHistory(new LinkedList<>());
                        userMap.put(user.getUserName(), user);
                        final String statusUser = "User was connected: " + user.getUserName() + "\n";
                        generalInfo.add(statusUser);
                        if (activeUser == null) outPutTextArea.append(statusUser);
                    }
                }
                return;
            } catch (Exception e) {
                System.out.println("Exception when get message from user:" + user.getUserName());
            }
        }

        if (message instanceof List) {
            System.out.println("list of users has arrived");
            List<User> availableUsers = (List<User>) message;
            availableUsers.forEach(user -> {
                user.setUserChatHistory(new LinkedList<>());
                graphicList.add(graphicList.getSize(), user.getUserName());
                userMap.put(user.getUserName(), user);
            });
        }

        if (message instanceof Message) {
            System.out.println("InputMessage");
            final Message msg = (Message) message;
            final String outMsgPattern = msg.getFrom() + ": " + msg.getMessage() + "\n";
            final User sender = userMap.get(msg.getFrom());
            sender.getUserChatHistory().add(outMsgPattern);

            System.out.println("activeUser has got message:" + activeUser);
            if (activeUser != null && Objects.equals(sender, activeUser)) outPutTextArea.append(outMsgPattern);
        }
    }

    private void handleConnectable(final SelectionKey key) throws IOException {

        final SocketChannel channel = (SocketChannel) key.channel();
        if (channel.isConnectionPending()) {
            channel.finishConnect();
        }
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
    }

    private static SocketChannel openConnection() throws IOException {

        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.bind(new InetSocketAddress("0.0.0.0", findAvailablePort()));
        socketChannel.connect(new InetSocketAddress("176.37.243.58", 9999));
        socketChannel.configureBlocking(false);

        while (!socketChannel.finishConnect()) {
            System.out.println("waiting connection....");
        }
        return socketChannel;
    }

    public void setOutPutTextArea(TextArea outPutTextArea) {
        this.outPutTextArea = outPutTextArea;
    }

    public TextArea getOutPutTextArea() {
        synchronized (obj) {
            return outPutTextArea;
        }
    }

    public void setGraphicList(DefaultListModel<String> graphicList) {
        this.graphicList = graphicList;
    }

    public MessageWriter getMessageWriter() {
        return messageWriter;
    }

    public ConcurrentHashMap<String, User> getUserMap() {
        return userMap;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public List<String> getGeneralInfo() {
        return generalInfo;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
}

