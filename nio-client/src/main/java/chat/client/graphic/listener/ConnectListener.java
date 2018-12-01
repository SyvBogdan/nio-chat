package chat.client.graphic.listener;

import chat.client.model.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static chat.client.graphic.listener.UserFieldListener.DEFAULT_TEXT;
import static chat.client.graphic.listener.UserFieldListener.EMPTY;
import static chat.client.util.Util.getLocalAddress;

public class ConnectListener implements MouseListener {

    private final ChatClient chatClient;

    private final JTextField textField;

    private final TextArea textArea;

    private final JButton disconnectButton;

    public ConnectListener(ChatClient chatClient, JTextField textField, TextArea textArea, JButton disconnectButton) {
        this.chatClient = chatClient;
        this.textField = textField;
        this.textArea = textArea;
        this.disconnectButton = disconnectButton;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        if (e.getComponent().isEnabled()) {
            final String login = textField.getText();
            if (login.equals(EMPTY) || login.equals(DEFAULT_TEXT)) {
                textArea.append("Please, write your username\n");
            } else {
                boolean result = chatClient.startClient(login);
                if (result) {

                    final String ip = getLocalAddress(chatClient.getSocketChannel()).getAddress().toString().replace("/","");


                    final String user = textField.getText();

                    textField.setText(user + "(" + ip + ")");

                    textField.setEditable(false);
                    textField.setSelectedTextColor(Color.BLACK);
                    textField.setFont(new Font("Verdana", Font.BOLD, 10));
                    textField.setBackground(Color.WHITE);
                    e.getComponent().setEnabled(false);
                    disconnectButton.setEnabled(true);
                }
            }
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
