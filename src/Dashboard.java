import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;

public class Dashboard {
  private JFrame frame;
  private String username;
//  private JTextPane chatArea;
  private JTextField inputField;
  private Client client;
  private final UserColorManager colorManager = new UserColorManager();
  private JPanel messagePanel;


  public  Dashboard(String username, Client client){
    this.username = username;
    this.client = client;

    frame = new JFrame("Chatroom - "+ username);
    frame.setSize(400,600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initializeUI();
    listenForMessage();
    show();
  }

  private void initializeUI(){
    JPanel panel = new JPanel(new BorderLayout());

    //this is the message panel setup
    messagePanel = new JPanel();
    messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
    messagePanel.setBackground(Color.WHITE);

    JScrollPane scrollPane = new JScrollPane(messagePanel);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    panel.add(scrollPane, BorderLayout.CENTER);

    //this is the input area

    JPanel inputPanel = new JPanel(new BorderLayout());
    inputField = new JTextField();
    JButton sendButton = new JButton("Send");


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
    //the format is "Morris: Hello there!"
    if(msg.contains(":")){
      int colonIndex = msg.indexOf(":");
      String user = msg.substring(0, colonIndex).trim();
      String message = msg.substring(colonIndex + 1).trim();
      Color userColor = colorManager.getColorForUser(user);
      boolean isSender = user.equalsIgnoreCase(username);
      addMessageBubble(user + ": " + message, userColor, isSender);
//      appendStyled(user + ": ", userColor);
//      appendStyled(message + "\n", Color.BLACK);
    } else {
      // this will notify if someone has joined the chat that is "Morris joined the chat!"
//      appendStyled(msg + "\n", Color.GRAY);
      addMessageBubble(msg, Color.GRAY, false);
    }
  }

  private void addMessageBubble(String message, Color color, boolean isSender){
    SwingUtilities.invokeLater(()->
      {
        JPanel bubble = new JPanel(new BorderLayout());
        bubble.setBackground(color);
        bubble.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        JLabel msgLabel = new JLabel("<html><p style='width:200px;'>" + message + "</p></html>");
        msgLabel.setForeground(color.WHITE);

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

      }
    );
  }
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
  }
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
