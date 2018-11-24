package chat.client.model;



import chat.model.Message;
import chat.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static chat.client.util.Util.findAvailablePort;


public class ChatClient {

    private User myUser;

    public User getLocalUser() {
        return myUser;
    }

    private DefaultListModel<String> graphicList;

    private Selector selector;

    private ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<>();

    private MessageWriter messageWriter;

    private TextArea outPutTextArea;

    public void startClient(final String userName) {

        try {
            final SocketChannel socketChannel = openConnection();
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

            myUser = new User(userName, (InetSocketAddress) socketChannel.getLocalAddress());
            messageWriter = new MessageWriter(socketChannel);

            //fire register me on the server
            messageWriter.writeToServer(myUser);

            new Thread(this::startServerListening).start();

        } catch (Throwable e) {
            outPutTextArea.append(e.getMessage());
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

                    if(currentKey.isWritable()){
                        currentKey.interestOps(SelectionKey.OP_READ);
                    }

                }
            }
        } catch (Exception e) {
            outPutTextArea.append(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void handleRead(final SelectionKey key) throws IOException {
        System.out.println("InputMessage");

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

            final User user = (User) message;
            if (user.isConnected()) {

                if (user.equals(myUser)) {
                    outPutTextArea.append("I am connected :)\n");
                } else {
                    graphicList.add(graphicList.getSize(), user.getUserName());
                    userMap.put(user.getUserName(), user);
                    outPutTextArea.append("User was connected: " + user.getUserName() + "\n");
                }

            }

            return;
        }

        if (message instanceof List) {
            System.out.println("list of users has arrived");
            List<User> availableUsers = (List<User>) message;
            availableUsers.forEach(user -> {
                graphicList.add(graphicList.getSize(), user.getUserName());
                userMap.put(user.getUserName(), user);
            });
        }

        if (message instanceof Message) {
            final Message msg = (Message) message;

            System.out.println(msg);
            outPutTextArea.append(msg.getFrom() + ": " + msg.getMessage() + "\n");

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

        socketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), findAvailablePort()));
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
        return outPutTextArea;
    }

    public void setGraphicList(DefaultListModel<String> graphicList) {
        this.graphicList = graphicList;
    }

    public MessageWriter getMessageWriter() {
        return messageWriter;
    }

}

