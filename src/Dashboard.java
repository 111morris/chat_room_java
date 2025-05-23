import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Dashboard extends JFrame {
  private JFrame frame;
  private String username;
  private JTextField inputField;
  private Client client;
  private final UserColorManager colorManager = new UserColorManager();
  private JPanel messagePanel;
  private JTextArea chatArea;
  private JTextField messageField;

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
    show();
  }

  private void createChatDisplayArea() {
    JPanel panel = new JPanel(new BorderLayout());

    chatArea = new JTextArea();
    chatArea.setEditable(false);
    JScrollPane scrollPane1 = new JScrollPane(chatArea);
    add(scrollPane1, BorderLayout.CENTER);


    //this is the message panel setup
    messagePanel = new JPanel();
    messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
    messagePanel.setBackground(Color.WHITE);

    scrollPane = new JScrollPane(messagePanel);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    panel.add(scrollPane, BorderLayout.CENTER);
  }


  public void createInputArea(){

    messageField = new JTextField();
    sendButton = new JButton("Send");

    JPanel inputPanel = new JPanel(new BorderLayout());
    inputPanel.add(messageField, BorderLayout.CENTER);
    inputPanel.add(sendButton, BorderLayout.EAST);

    add(inputPanel, BorderLayout.SOUTH);

    sendButton.adActionListener(e -> sendMessage());
    messageField.addActionListener(e -> sendMessage());



    sendButton.addActionListener(e -> sendMessage());
    inputField.addActionListener(e-> sendMessage());

    inputPanel.add(inputField, BorderLayout.CENTER);
    inputPanel.add(sendButton, BorderLayout.EAST);

    panel.add(inputPanel, BorderLayout.SOUTH);
    frame.add(panel);

  }
  private void sendMessage() {
    String message = inputField.getText().trim();
    if (!message.isEmpty()){
      if(message.equalsIgnoreCase("/quit")){
        client.sendMessage(message);
        addMessageBubble("You have left the chat.", Color.GRAY, true);
        client.close();
        frame.dispose();
        return;
      }
      client.sendMessage(message);
      inputField.setText("");
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
        JScrollPane scrollPane1 = (JScrollPane) messagePanel.getParent().getParent();
        JScrollBar vertical = scrollPane1.getVerticalScrollBar();
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
  public void show() {
    frame.setVisible(true);
  }
}
