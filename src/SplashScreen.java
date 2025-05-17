import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SplashScreen {
  private JFrame frame;
  private JTextField usernameField;
  private JTextField serverPortField;

  // you should add ip address input

  public SplashScreen(){
    frame = new JFrame("Chatroom Setup");
    frame.setSize(300,200);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new GridLayout(4,2,10,10));

    //adding the labels
    //username input
    JLabel userNameLabel = new JLabel("Username: ");
    usernameField = new JTextField();

    JLabel portLabel = new JLabel("Server port: ");
    serverPortField = new JTextField();

    //add userver ip input

    JButton connectionButton = new JButton("Connect");

    connectionButton.addActionListener(this::handleConnect);

    frame.add(userNameLabel);
    frame.add(usernameField);
    frame.add(portLabel);
    frame.add(serverPortField);
    // you will add iplabel and ipaddressfield

    frame.add(new JLabel()); //Empty cell
    frame.add(connectionButton);

    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);
  }
  private void handleConnect(ActionEvent e) {
    String username = usernameField.getText().trim();
    String portText = serverPortField.getText().trim();
    //you will add host = ipaddressfiled
    if(username.isEmpty() || portText.isEmpty()) {
      JOptionPane.showMessageDialog(frame, "Please enter both username and port.");
      return;
    }
  }
  public static void main(String[] args) {

  }

}