package chat.client.graphic.listener;

import chat.client.model.ChatClient;
import chat.model.Message;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class InputTextAreaListener implements KeyListener {

    private final TextArea inputTextArea;
    private final ChatClient chatClient;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        if (!Objects.isNull(to)) {
            chatClient.setActiveUser(chatClient.getUserMap().get(to));
            this.to = to;
        }
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
            final String raw = "From me" + ": " + text + "\n";
            chatClient.getActiveUser().getUserChatHistory().add(raw);
            chatClient.getOutPutTextArea().append(raw);
            chatClient.getMessageWriter().writeToServer(message);

            inputTextArea.setText("");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
