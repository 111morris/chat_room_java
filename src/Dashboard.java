import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Dashboard extends JFrame {
  private String username = "morris";
  private Client client;
  private final UserColorManager colorManager = new UserColorManager();
  private JTextArea chatArea;
  private JTextField messageField;
  private JButton sendButton;
  private JPanel messagePanel;
  private JScrollPane scrollPane;
  private boolean displayBubble = true;

  public void setUsername(String username) {
    this.username = username;
  }
  
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
      messagePanel.setBackground(new Color(245, 247, 250));
      messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      
      scrollPane = new JScrollPane(messagePanel);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      scrollPane.setBorder(BorderFactory.createEmptyBorder()); // remove ugly default border
      scrollPane.getVerticalScrollBar().setUnitIncrement(16); // smooth scrolling
      add(scrollPane, BorderLayout.CENTER);
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
    messageField = new ModernTextField();
    messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    
    ModernButton sendBtn = new ModernButton("Send");
    sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    sendBtn.setBackground(new Color(41, 128, 185));
    sendBtn.setForeground(Color.WHITE);
    sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    sendBtn.setPreferredSize(new Dimension(80, 40));
    
    sendBtn.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            sendBtn.setBackground(new Color(52, 152, 219));
        }
        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            sendBtn.setBackground(new Color(41, 128, 185));
        }
    });

    sendButton = sendBtn;
    
    JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
    inputPanel.setBackground(new Color(236, 240, 241));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    inputPanel.add(messageField, BorderLayout.CENTER);
    inputPanel.add(sendButton, BorderLayout.EAST);
    add(inputPanel, BorderLayout.SOUTH);
    
    sendButton.addActionListener(e->sendMessage());
    messageField.addActionListener(e->sendMessage());
  }
  private void sendMessage() {
    String message = messageField.getText().trim();
    if (!message.isEmpty()) {
      if (message.equalsIgnoreCase("/quit")) {
        client.sendMessage(message);
        addMessageBubble("[" + getCurrentTime() + "] You have left the chat.", Color.GRAY, true);
        client.close();
        return;
      }
      // Send to server without timestamp
      client.sendMessage(message);

      // Show with timestamp locally
      String displayMessage = "[" + getCurrentTime() + "] " + username + ": " + message;

      if (displayBubble) {
        addMessageBubble(displayMessage, colorManager.getColorForUser(username), true);
      } else {
        chatArea.append(displayMessage + "\n");
      }
      messageField.setText("");
    }
  }
  private String getCurrentTime() {
    java.time.LocalTime now = java.time.LocalTime.now();
    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a");
    return now.format(formatter);
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

  private void print_hello() {
    System.out.println("hello world");
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
        SwingUtilities.invokeLater(() -> {
          // Format the current time

          if (displayBubble) {
            boolean isNotification = color.equals(Color.GRAY);
            
            JLabel msgLabel = new JLabel("<html><p style='width: 180px;'>" + message + "</p></html>");
            if (isNotification) {
                msgLabel.setForeground(new Color(100, 100, 100)); // lighter text for notifications
                msgLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                msgLabel.setForeground(isSender ? Color.WHITE : new Color(44, 62, 80));
                msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
            msgLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

            JPanel bubble = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                    g2.dispose();
                }
            };
            bubble.setOpaque(false);
            
            if (isNotification) {
                 bubble.setBackground(new Color(236, 240, 241)); // light gray bubble for notification
            } else if (isSender) {
                 bubble.setBackground(new Color(41, 128, 185)); // modern blue for sender
            } else {
                 // for others, use a very light version of their user color, or white
                 float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                 Color pastelColor = Color.getHSBColor(hsb[0], 0.15f, 1.0f); // very light pastel
                 bubble.setBackground(pastelColor);
            }

            bubble.add(msgLabel, BorderLayout.CENTER);
            bubble.setMaximumSize(new Dimension(280, Integer.MAX_VALUE));
            
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
            wrapper.setOpaque(false);
            wrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // space between messages
            
            if (isNotification) {
              wrapper.add(Box.createHorizontalGlue());
              wrapper.add(bubble);
              wrapper.add(Box.createHorizontalGlue());
            } else if (isSender) {
              wrapper.add(Box.createHorizontalGlue());
              wrapper.add(bubble);
            } else {
              wrapper.add(bubble);
              wrapper.add(Box.createHorizontalGlue());
            }
            messagePanel.add(wrapper);
            messagePanel.revalidate();
            messagePanel.repaint();
          } else {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
          }
          JScrollBar vertical = scrollPane.getVerticalScrollBar();
          vertical.setValue(vertical.getMaximum());
        });
      }
    );
  }
}
