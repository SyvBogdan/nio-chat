package chat.client.graphic.listener;

import chat.client.model.ChatClient;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

public class SendListener implements MouseListener, Writable {


    private final TextArea inputTextArea;
    private final ChatClient chatClient;
    private String to;

    public String getTo() {
        return to;
    }

    public SendListener(TextArea inputTextArea, ChatClient chatClient) {
        this.inputTextArea = inputTextArea;
        this.chatClient = chatClient;
    }

    public void setTo(String to) {
        if (!Objects.isNull(to)) {
            chatClient.setActiveUser(chatClient.getUserMap().get(to));
            this.to = to;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        pushMessage(to, inputTextArea, chatClient);
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
