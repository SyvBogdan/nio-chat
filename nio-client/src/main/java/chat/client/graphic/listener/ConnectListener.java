package chat.client.graphic.listener;

import chat.client.model.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static chat.client.graphic.listener.UserFieldListener.DEFAULT_TEXT;
import static chat.client.graphic.listener.UserFieldListener.EMPTY;

public class ConnectListener implements MouseListener {

    private final ChatClient chatClient;

    private final JTextField textField;

    private final TextArea textArea;

    public ConnectListener(ChatClient chatClient, JTextField textField, TextArea textArea) {
        this.chatClient = chatClient;
        this.textField = textField;
        this.textArea = textArea;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        final String login = textField.getText();
        if (login.equals(EMPTY) || login.equals(DEFAULT_TEXT)) {
            textArea.append("Please, write your username\n");
        } else {
            chatClient.startClient(login);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
