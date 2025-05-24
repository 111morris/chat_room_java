import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.Wrapper;

public class Dashboard extends JFrame {
  private String username = "morris";
  private Client client;
  private final UserColorManager colorManager = new UserColorManager();
  private JTextArea chatArea;
  private JTextField messageField;
  private JButton sendButton;
  private JPanel messagePanel;
  private JScrollPane scrollPane;
  private boolean displayBubble = false;

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
    if(displayBubble){
      messagePanel = new JPanel();
      messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
      scrollPane = new JScrollPane(messagePanel);

      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      add(scrollPane, BorderLayout.CENTER);  messagePanel.setBackground(Color.WHITE);
    } else {
      chatArea = new JTextArea();
      chatArea.setEditable(false);
      chatArea.setLineWrap(true);
      chatArea.setWrapStyleWord(true);

      scrollPane = new JScrollPane(chatArea);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      add(scrollPane, BorderLayout.CENTER);
    }
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
        //dispose();
        return;
      }
      client.sendMessage(message);
      if(displayBubble) {
        addMessageBubble(username + ": " + message, colorManager.getColorForUser(username), true);
      }else {
        chatArea.append(username + ": " + message + "\n");
      }
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
        if(displayBubble) {

          //JPanel bubble = new JPanel(new BorderLayout());
          //bubble.setBackground(color);
          //bubble.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

          JLabel msgLabel = new JLabel("<html>" + message + "</html>");
          msgLabel.setForeground(Color.WHITE);
          msgLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
          msgLabel.setFont(new Font("Arial", Font.PLAIN, 14));
          msgLabel.setOpaque(false);

          //this will create the bubble and then add the label
          JPanel bubble = new JPanel(new BorderLayout());
          bubble.setLayout(new BoxLayout(bubble, BoxLayout.X_AXIS));
          bubble.setBackground(color);
          bubble.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
          bubble.setMaximumSize(new Dimension(280, Integer.MAX_VALUE));
          bubble.add(msgLabel);
          bubble.setAlignmentX(Component.LEFT_ALIGNMENT);
          //this is for the rounded cornersb
          bubble.setBorder(BorderFactory.createLineBorder(color.darker(), 1, true));


          //this will wrap bubble in alignment panel
          JPanel wrapper = new JPanel();
          wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
          wrapper.setOpaque(false);
          //this is the spacing between the bubbles
          //wrapper.setBorder(BorderFactory.createEmptyBorder(4,10,4,10));
        } else {
          chatArea.append(message + "\n");
          chatArea.setCaretPosition(chatArea.getDocument().getLength());
        }

        if(isSender) {
          //wrapper.add(Box.createHorizontalGlue());
          wrapper.add(bubble);
        }else {
          wrapper.add(bubble);
          //wrapper.add(Box.createHorizontalGlue());
        }
        // this is were you will add the main panel
        messagePanel.add(wrapper);
        messagePanel.revalidate();
        messagePanel.repaint();

        // auto-scroll to bottom
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

      }
    );
  }
}
