package chat.client.graphic;

import chat.client.graphic.listener.ConnectListener;
import chat.client.graphic.listener.InputTextAreaListener;
import chat.client.graphic.listener.UserFieldListener;
import chat.client.graphic.listener.UserListMouseListener;
import chat.client.model.ChatClient;

import javax.swing.*;
import java.awt.*;


import static chat.client.graphic.listener.UserFieldListener.DEFAULT_TEXT;
import static java.awt.GridBagConstraints.NORTH;

public class ChatPanel extends JPanel {
    private static final long serialVersionUID = -1997891272226170367L;

    //final LayoutManager layout;

    public ChatPanel(LayoutManager layout) {

        super(layout);
        //this.layout = layout;
        setVisible(true);
    }

    public ChatPanel(final ChatClient chatClient) {
        final GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);
        this.setBorder(BorderFactory.createTitledBorder(" "));

        GridBagConstraints c = new GridBagConstraints();

        //add TextInput
        final JTextField textField = new JTextField(DEFAULT_TEXT);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        // c.ipady =5;
        c.insets = new Insets(0, 10, 10, 30);
        this.add(textField, c);


        //add inner panel
        JPanel innerJpanel = new JPanel();
        final GridBagLayout innnerGridBagLayout = new GridBagLayout();
        innerJpanel.setLayout(innnerGridBagLayout);
        GridBagConstraints innerc = new GridBagConstraints();
        c.gridx = 5;
        c.gridy = 0;


        //add button to connect
        final JButton conButton = new JButton("Connect");
        innerc.fill = GridBagConstraints.HORIZONTAL;
        innerc.gridx = 0;
        innerc.gridy = 0;
        innerc.ipadx = 0;
        innerc.insets = new Insets(0, 20, 10, 20);
        innerJpanel.add(conButton, innerc);

        //add button to disconnect
        final JButton disConButton = new JButton("Disconnect");
        innerc.fill = GridBagConstraints.HORIZONTAL;
        innerc.gridx = 1;
        innerc.gridy = 0;
        innerc.ipady = 0;
        innerc.insets = new Insets(0, 20, 10, 20);
        innerJpanel.add(disConButton, innerc);
        this.add(innerJpanel, c);

        //add userlist
        final DefaultListModel<String> model = new DefaultListModel<>();
        model.add(0, "Available users:");
        final JList<String> userList = new JList<>(model);
        final JScrollPane scrollPane1 = new JScrollPane(userList);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 0);
        c.weightx = 0.01;
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 10;
        c.anchor = NORTH;
        this.add(scrollPane1, c);
        c.insets = new Insets(0, 10, 0, 10);

        //add text output area
        final TextArea textArea = new TextArea();
        final JScrollPane textAreaScrollPane1 = new JScrollPane(textArea);
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 5;
        c.gridy = 1;
        this.add(textAreaScrollPane1, c);

        //add button to send
        final JButton button = new JButton("Send");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        c.ipady = 30;
        c.insets = new Insets(10, 0, 0, 0);
        this.add(button, c);

        //add Input Panel

        final TextArea inputArea = new TextArea();
        //final JScrollPane inputScrollPane = new JScrollPane(inputArea);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 5;
        c.gridy = 2;
        c.ipady = -100;
        c.insets = new Insets(10, 10, 0, 10);
        this.add(inputArea, c);

        setPreferredSize(getSize());
        setVisible(true);

        ///listeners

        textField.addMouseListener(new UserFieldListener(textField));
        conButton.addMouseListener(new ConnectListener(chatClient, textField, textArea));

        final InputTextAreaListener inputTextAreaListener = new InputTextAreaListener(inputArea, chatClient);
        inputArea.addKeyListener(inputTextAreaListener);
        userList.addMouseListener(new UserListMouseListener(inputTextAreaListener));

        chatClient.setOutPutTextArea(textArea);
        chatClient.setGraphicList(model);
    }

}
