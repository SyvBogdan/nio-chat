package chat.client.graphic.listener;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UserListMouseListener implements MouseListener {

    private final InputTextAreaListener inputWriteListener;

    public UserListMouseListener(InputTextAreaListener inputWriteListener) {
        this.inputWriteListener = inputWriteListener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final JList<String> list = (JList) e.getSource();
        int index = list.locationToIndex(e.getPoint());
        final String activeUser = (String) list.getModel().getElementAt(index);
        inputWriteListener.setTo(activeUser);
        System.out.println("activeUser: " + activeUser);
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
