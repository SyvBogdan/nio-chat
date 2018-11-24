package chat.client.graphic.listener;

import chat.client.model.ChatClient;
import chat.model.Message;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputTextAreaListener implements KeyListener {

    private final TextArea inputTextArea;
    private final ChatClient chatClient;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    private String to;

    public InputTextAreaListener(TextArea textArea, ChatClient chatClient) {
        this.inputTextArea = textArea;
        this.chatClient = chatClient;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            final String text = inputTextArea.getText();

            if (to == null) {
                System.out.println("Receiver can't be null");
                return;
            }

            final Message message = new Message(chatClient.getLocalUser().getUserName(), to, text);
            chatClient.getMessageWriter().writeToServer(message);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
