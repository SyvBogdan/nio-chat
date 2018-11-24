package chat.client.graphic;

import chat.client.util.Util;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 5345069910976993720L;

    private static final int WIDTH = 500;
    private static final int HEIGHT = 600;

    private static final String RESOURCE ="acceptable.png";

    private final JPanel jpanel;

    public MainFrame(String title,JPanel jpanel, int width, int height) throws MalformedURLException {

        this.setBounds(100, 200, width, height);
        setMaximumSize( new Dimension(width,height));
        setTitle(title);
        setIconImage(Util.getImage(RESOURCE));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.jpanel = jpanel;
        setContentPane(jpanel);
       // this.pack();
        setVisible(true);

    }

    public JPanel getJpanel() {
        this.setBounds(100, 200, WIDTH, HEIGHT);
        return jpanel;
    }
}
