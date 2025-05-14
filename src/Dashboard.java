import javax.swing.*;
import java.awt.*;

public class Dashboard {
  private JFrame frame;
  public  Dashboard(){
    frame = new JFrame("Chatroom Dashboard");
    frame.setSize(400,400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initializeUI();
  }
  private void initializeUI(){
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    //chat area
    JTextPane chatArea = new JTextPane();
    chatArea.setEditable(false);
    panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

    //this will be the input area
    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BorderLayout());

    JTextField inputField = new JTextField();


  }

  public void show() {
    frame.setVisible(true);
  }
}
