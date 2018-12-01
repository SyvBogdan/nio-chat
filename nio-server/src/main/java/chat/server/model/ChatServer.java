package chat.server.model;


import chat.model.Message;
import chat.model.User;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static chat.server.util.ServerHelper.getAddress;


public class ChatServer implements IChatServer, Runnable {

    private final InetSocketAddress serverAdress;

    private final Selector serverSelector;

    private final ServerSocketChannel serverSocketChannel;

    private final ConcurrentHashMap<String, UserProfile> clientMap = new ConcurrentHashMap<>();

    private final ExecutorService writeExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ChatServer(InetSocketAddress serverAdress) throws IOException {
        this.serverAdress = serverAdress;

        //open server selector to manage channels as server so clients
        this.serverSelector = SelectorProvider.provider().openSelector();

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(serverAdress);
        serverSocketChannel.configureBlocking(false);

        // then register it in serverSelector to check in loop for Acceptable event(when client is trying to connect)
        serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
    }

    public void startServer() throws IOException {
        new Thread(this).start();
    }

    @Override
    public void run() {

        while (serverSelector.isOpen()) {

            //serverSelector.select()
            /*
             *selector have to make iterator to look through set of ready for I/O operations
             * Endless loop will look through this Set<SelectionKey> and the event that caused it and
             * do some actions
             */
            try {
                serverSelector.select(500);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final Iterator<SelectionKey> selectionKeyIterator = serverSelector.selectedKeys().iterator();

            while (selectionKeyIterator.hasNext()) {

                final SelectionKey key = selectionKeyIterator.next();

                if (!key.isValid() && key.channel().isRegistered()) continue;

                if (key.isAcceptable()) {
                    handleAcceptClientEvent(key);
                }
                if (key.isReadable()) {
                    handleRead(key);
                }
                /*
                 * drop used operation from Set and it seems to be finished after this for server logic
                 * we have to remove it ourselves because when this SelectionKey will generate another event
                 *
                 */
                selectionKeyIterator.remove();
            }
        }
    }

    /**
     * @param key --- the method will be invoked after
     */
    @Override
    public void handleAcceptClientEvent(SelectionKey key) {

        final SelectableChannel channel = key.channel();
        if (channel instanceof ServerSocketChannel) {
            try {
                final SocketChannel clientChannel = ((ServerSocketChannel) channel).accept();
                clientChannel.configureBlocking(false);
                final InetSocketAddress clientAddress = (InetSocketAddress) clientChannel.getRemoteAddress();

                //clientMap.put(getAddress(clientAddress), clientChannel);
                clientChannel.register(serverSelector, SelectionKey.OP_READ, null);

                System.out.println("Channel with address: " + getAddress(clientAddress) + " was successfully accepted.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleRead(SelectionKey key) {

        final SocketChannel socketChannel = (SocketChannel) key.channel();
        Object message = null;

        try {
            final ByteBuffer buf = ByteBuffer.allocate(10000);
            socketChannel.read(buf);
            buf.flip();

            final byte[] arrByte = new byte[buf.limit()];
            buf.get(arrByte);

            if (arrByte.length == 0) {
                skipConnection(key, socketChannel);
                return;
            }

            ByteArrayInputStream in = new ByteArrayInputStream(arrByte);
            ObjectInputStream is = new ObjectInputStream(in);
            message = is.readObject();

            //process message after deserialization
            provideMessageLogic(message, socketChannel, key);

        } catch (Exception e) {
            if (socketChannel.isConnected()) {
                try {
                    if (message instanceof User) {
                        final User user = (User) message;
                        clientMap.remove(user.getUserName());
                    }
                    socketChannel.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            key.cancel();
        }
    }

    private void skipConnection(final SelectionKey key ,final SocketChannel socketChannel){

       if (socketChannel.isOpen()) {
           try {

              final String username = (String) key.attachment();
               final UserProfile profile = clientMap.get(username);

               final User unActiveUser = profile.getUser();
               unActiveUser.setConnected(false);
               clientMap.remove(username);
               clientMap.forEach((evUser, prf) -> writeToClientAsync(unActiveUser, prf.getUserChannel()));
               socketChannel.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }

    private void provideMessageLogic(final Object message, final SocketChannel socketChannel, final SelectionKey key) {

        if (message instanceof Message) {
            final Message msg = (Message) message;

            final UserProfile userProfile = clientMap.get(msg.getTo());
            writeToClientAsync(msg, userProfile.getUserChannel());
            return;
        }

        if (message instanceof User) {
            final User user = (User) message;

            System.out.println(user);

            if (clientMap.get(user.getUserName()) == null) {
                key.attach(user.getUserName());
                clientMap.put(user.getUserName(), new UserProfile(user, socketChannel));
                user.setConnected(true);
                clientMap.forEach((usr, profile) -> writeToClientAsync(user, profile.getUserChannel()));

                final List<User> availableUsers = clientMap.entrySet().stream()
                        .filter(us -> !us.getValue().getUser().equals(user))
                        .map(entry -> entry.getValue().getUser()).collect(Collectors.toList());

                System.out.println("write to user: " + user + " all available users " + availableUsers);
                writeToClientAsync(availableUsers, socketChannel);

            } else {
                writeToClientAsync(user, socketChannel);
            }
        }
    }

    private void writeToClientAsync(final Object message, final SocketChannel socketChannel) {

        CompletableFuture.runAsync(
                () -> {
                    final ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try (ObjectOutputStream os = new ObjectOutputStream(out)) {
                        os.writeObject(message);
                        socketChannel.write(ByteBuffer.wrap(out.toByteArray()));
                        handleWrite(socketChannel.keyFor(serverSelector));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                writeExecutor
        );
    }

    @Override
    public void handleWrite(SelectionKey key) {
        key.interestOps(SelectionKey.OP_READ);
    }

}
