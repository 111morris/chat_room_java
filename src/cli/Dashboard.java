package cli;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
  private JTextArea chatArea;
  private JTextField messageField;
  private JButton sendButton;
  private Client client;

  public Dashboard() {
    setTitle("Chat Room");
    setSize(400, 500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    chatArea = new JTextArea();
    chatArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(chatArea);
    add(scrollPane, BorderLayout.CENTER);

    messageField = new JTextField();
    sendButton = new JButton("Send");

    JPanel inputPanel = new JPanel(new BorderLayout());
    inputPanel.add(messageField, BorderLayout.CENTER);
    inputPanel.add(sendButton, BorderLayout.EAST);

    add(inputPanel, BorderLayout.SOUTH);

    sendButton.addActionListener(e -> sendMessage());
    messageField.addActionListener(e -> sendMessage());

    setVisible(true);
  }

  private void sendMessage() {
    String message = messageField.getText().trim();
    if(!message.isEmpty()){
      if(message.equalsIgnoreCase("/leave")){
        client.sendMessage(message);
        client.close();
        return;
      }

      //send to server without timestamp
      client.sendMessage(message);
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(Dashboard::new);
  }
}
