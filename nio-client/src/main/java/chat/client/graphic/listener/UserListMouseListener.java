package chat.client.graphic.listener;

import chat.client.model.ChatClient;
import chat.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Objects;

public class UserListMouseListener implements MouseListener {

    private final InputTextAreaListener inputWriteListener;

    private final SendListener sendListener;

    private final ChatClient chatClient;

    public UserListMouseListener(InputTextAreaListener inputWriteListener, SendListener sendListener ,ChatClient chatClient) {
        this.inputWriteListener = inputWriteListener;
        this.chatClient = chatClient;
        this.sendListener = sendListener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final JList<String> list = (JList) e.getSource();
        int index = list.locationToIndex(e.getPoint());
        final String destUser = list.getModel().getElementAt(index);
        inputWriteListener.setTo(destUser);
        sendListener.setTo(destUser);

        final User activeUser = chatClient.getActiveUser();
        final TextArea textArea = chatClient.getOutPutTextArea();
        textArea.setText("");

        if(destUser!= null && destUser.equals("Available users:")){
            textArea.setText("General info:" + "\n");
            chatClient.getGeneralInfo().forEach(textArea::append);
            System.out.println(chatClient.getGeneralInfo());
            return;
        }

        if (!Objects.isNull(activeUser)) {
            textArea.setText("Conversation with "+ activeUser.getUserName() + ": \n");
            if (Objects.isNull(activeUser.getUserChatHistory())) {
                activeUser.setUserChatHistory(new LinkedList<>());
            }
            activeUser.getUserChatHistory().forEach(textArea::append);
        }

        System.out.println("activeUser: " + destUser);
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
