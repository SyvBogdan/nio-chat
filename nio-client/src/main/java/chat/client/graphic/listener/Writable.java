package chat.client.graphic.listener;

import chat.client.model.ChatClient;
import chat.model.Message;

import java.awt.*;

public interface Writable {

    default  void pushMessage(final String to, final TextArea inputTextArea, final ChatClient chatClient) {
        final String text = inputTextArea.getText();
        if (to == null) {
            return;
        }
        final Message message = new Message(chatClient.getLocalUser().getUserName(), to, text);
        final String raw = "From me" + ": " + text + "\n";
        chatClient.getActiveUser().getUserChatHistory().add(raw);
        chatClient.getOutPutTextArea().append(raw);
        chatClient.getMessageWriter().writeToServer(message);

        inputTextArea.setText("");
        inputTextArea.setCaretPosition(0);
    }

}
