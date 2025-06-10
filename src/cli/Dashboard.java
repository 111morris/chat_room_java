package cli;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
  private JTextArea chatArea;
  private JTextField messageField;
  private JButton sendButton;

  public Dashboard() {

  }

  private void sendMessage() {
    String text = messageField.getText().trim();
    if (!text.isEmpty()) {
      chatArea.append("You: " + text + "\n");
      messageField.setText("");
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(Dashboard::new);
  }
}
