package chat.client.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class MessageWriter {

    private final SocketChannel socketChannel;

    public MessageWriter(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void writeToServer(Object clientMessage) {
        try {
            socketChannel.write(ByteBuffer.wrap(createMessage(clientMessage).toByteArray()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private ByteArrayOutputStream createMessage(final Object message) {

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(out);
            os.writeObject(message);
            os.close();
            return out;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
