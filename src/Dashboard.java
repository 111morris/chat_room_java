import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

public class Dashboard {
  private JFrame frame;
  private String username;
  private JTextPane chatArea;
  private JTextField inputField;
  private Client client;

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

    chatArea = new JTextPane();
    chatArea.setEditable(false);
    panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

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
          appendToChat(msg);
        }
      }catch (IOException e) {
        appendToChat("Disconnected from the chat");
      }
    }).start();
  }

  private void appendToChat(String msg) {
    SwingUtilities.invokeLater(()->{
      chatArea.setText(chatArea.getText()+ msg + "\n");
    });
  }
  public void show() {
    frame.setVisible(true);
  }
}
