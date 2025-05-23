package cli;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
  private JTextField chatArea;
  private JTextField messageField;
  private JButton sendButton;

  public Dashboard() {
    setTitle("Java Chat Room");
    setSize(400,600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    //create a chat display area
    chatArea = new JTextField();
    chatArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(chatArea);
    add(scrollPane, BorderLayout.CENTER);

    //creating the input area(text field +button)
    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BorderLayout());

    messageField = new JTextField();
    sendButton = new JButton("Send");

    inputPanel.add(messageField, BorderLayout.CENTER);
    inputPanel.add(sendButton, BorderLayout.EAST);

    add(inputPanel, BorderLayout.SOUTH);



  }
}
