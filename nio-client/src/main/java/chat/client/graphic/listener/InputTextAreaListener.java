package chat.client.graphic.listener;

import chat.client.model.ChatClient;
import chat.model.Message;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class InputTextAreaListener implements KeyListener, Writable {

    private final TextArea inputTextArea;
    private final ChatClient chatClient;
    private String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        if (!Objects.isNull(to)) {
            chatClient.setActiveUser(chatClient.getUserMap().get(to));
            this.to = to;
        }
    }

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
            pushMessage(to, inputTextArea, chatClient);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
