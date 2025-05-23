import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Dashboard extends JFrame {
  private String username;
  private Client client;
  private final UserColorManager colorManager = new UserColorManager();
  private JTextArea chatArea;
  private JTextField messageField;
  private JButton sendButton;
  private JPanel messagePanel;
  private JScrollPane scrollPane;

  public Dashboard(String username, Client client){
    this.username = username;
    this.client = client;
    setTitle("Chatroom - "+ username);
    setSize(400,600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    createChatDisplayArea();
    createInputArea();
    listenForMessage();
    setVisible(true);
  }

  private void createChatDisplayArea() {
    messagePanel = new JPanel();
    messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
    messagePanel.setBackground(Color.WHITE);

    //chatArea = new JTextArea();
    //chatArea.setEditable(false);
    scrollPane = new JScrollPane(messagePanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    add(scrollPane, BorderLayout.CENTER);
 }

  public void createInputArea(){
    messageField = new JTextField();
    sendButton = new JButton("Send");
    JPanel inputPanel = new JPanel(new BorderLayout());
    inputPanel.add(messageField, BorderLayout.CENTER);
    inputPanel.add(sendButton, BorderLayout.EAST);
    add(inputPanel, BorderLayout.SOUTH);

    sendButton.addActionListener(e->sendMessage());
    messageField.addActionListener(e->sendMessage());

  }

  private void sendMessage() {
    String message = messageField.getText().trim();
    if (!message.isEmpty()){
      if(message.equalsIgnoreCase("/quit")){
        client.sendMessage(message);
        addMessageBubble("You have left the chat.", Color.GRAY, true);
        client.close();
        dispose();
        return;
      }
      client.sendMessage(message);
      //chatArea.append(username+": " + message + "\n");
      addMessageBubble(username + message, colorManager.getColorForUser(username), true);

      messageField.setText("");
    }
  }
  private void listenForMessage() {
    new Thread(()->{
      try{
        String msg;
        while ((msg = client.getIn().readLine()) != null){
          processAndAppendMessage(msg);
        }
      }catch (IOException e) {
//        appendStyled("Disconnected from the chat.", Color.GRAY);
        addMessageBubble("Disconnected from the chat.", Color.GRAY, false);
      }
    }).start();
  }



  private void processAndAppendMessage(String msg) {

    /**
     * handling of different types of messages:
     * 1. normal messages: format is "username: message"
     * 2. Notification message about nickname change: "Oldname is now known as newname"
     * 3. Join notification: "username joined the chat!"
     * 4. leave notification: "username left the chat"
     *
     * the notification are shown in gray color, normal messages in user specific colors.\
     *
     */
    //the format is "Morris: Hello there!"
    String trimmedMsg = msg.trim();

   //this will check for notification with specific phrasess
    if(trimmedMsg.endsWith(" joined the chat!") ||
        trimmedMsg.endsWith(" left the chat.") ||
        trimmedMsg.contains(" is now known as ")) {
      addMessageBubble(trimmedMsg, Color.GRAY, false);
      return;
    }

    // this will check if the message contains colon to parse user message
    if(trimmedMsg.contains(":")){
      int colonIndex = trimmedMsg.indexOf(":");
      String user = trimmedMsg.substring(0, colonIndex).trim();
      String message = trimmedMsg.substring(colonIndex + 1).trim();
      Color userColor = colorManager.getColorForUser(user);
      boolean isSender = user.equalsIgnoreCase(username);
      addMessageBubble(user + ": " + message, userColor, isSender);
    } else {
      // this is the unknown format, treat as notification
      addMessageBubble(trimmedMsg, Color.GRAY, false);
    }
  }

  private void addMessageBubble(String message, Color color, boolean isSender){
    SwingUtilities.invokeLater(()->
      {
        JPanel bubble = new JPanel(new BorderLayout());
        bubble.setBackground(color);
        bubble.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        JLabel msgLabel = new JLabel("<html><p style='width:200px;'>" + message + "</p></html>");
        msgLabel.setForeground(Color.WHITE);

        bubble.add(msgLabel, BorderLayout.CENTER);
        bubble.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        if(isSender) {
          wrapper.add(bubble, BorderLayout.EAST);
        }else {
          wrapper.add(bubble, BorderLayout.WEST);
        }
        messagePanel.add(wrapper);
        messagePanel.revalidate();
        messagePanel.repaint();

        //scroll to bottom
        //JScrollPane scrollPane1 = (JScrollPane) messagePanel.getParent().getParent();
        //JScrollBar vertical = scrollPane1.getVerticalScrollBar();
        //vertical.setValue(vertical.getMaximum());
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

      }
    );
  }
  /*
  private void appendStyled(String text, Color color) {
    SwingUtilities.invokeLater(()-> {
      StyledDocument doc = chatArea.getStyledDocument();
      Style style = chatArea.addStyle("ColorStyle", null);
      StyleConstants.setForeground(style, color);
      try{
        doc.insertString(doc.getLength(), text, style);
        chatArea.setCaretPosition(doc.getLength());
      } catch (BadLocationException e) {
       e.printStackTrace();
      }
    });
  }*/

  /*
  private void appendToChat(String msg) {
    SwingUtilities.invokeLater(()->{
      chatArea.setText(chatArea.getText()+ msg + "\n");
    });
  }*/

}
