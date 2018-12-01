package chat.client.graphic.listener;

import chat.client.model.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DisconnectListener implements MouseListener {

    private final ChatClient chatClient;

    private final JTextField textField;

    private final TextArea textArea;

    private final JButton connectButton;

    public DisconnectListener(ChatClient chatClient, JTextField textField, TextArea textArea, JButton connectButton) {
        this.chatClient = chatClient;
        this.textField = textField;
        this.textArea = textArea;
        this.connectButton = connectButton;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent().isEnabled()) {
            chatClient.closeConnection();
            textField.setText("");
            textField.setEditable(true);
            e.getComponent().setEnabled(false);
            connectButton.setEnabled(true);
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
