package chat.client.graphic.listener;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UserFieldListener implements MouseListener {

    public static final String DEFAULT_TEXT = "Enter your name, please";
    public static final String EMPTY = "";

    private final JTextField textField;

    public UserFieldListener(JTextField textField) {
        this.textField = textField;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (textField.getText().equals(DEFAULT_TEXT)) textField.setText(EMPTY);
    }

    @Override
    public void mouseExited(MouseEvent e) {
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
}
