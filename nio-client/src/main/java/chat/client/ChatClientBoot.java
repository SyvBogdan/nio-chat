package chat.client;

import chat.client.graphic.ChatPanel;
import chat.client.graphic.MainFrame;
import chat.client.model.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;

public class ChatClientBoot {

    public static void startClientGui(final ChatClient chatClient) throws MalformedURLException {

        final JPanel panel = new ChatPanel(chatClient);
        final MainFrame frame = new MainFrame("Nio Chat Client", panel, 700,400);
        final Container pane = frame.getContentPane();
        frame.setContentPane(pane);

    }

    public static void main(String[] args) throws IOException {
        final ChatClient chatClient = new ChatClient();
        startClientGui(chatClient);
    }
}
