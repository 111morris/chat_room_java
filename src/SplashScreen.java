import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SplashScreen {
  private JFrame frame;
  private JTextField usernameField;
  private JTextField serverPortField;
  private JTextField ipAddressField;

  private final Color placeholderColor = Color.GRAY;
  private final Color textColor = Color.BLACK;

  public SplashScreen(){
    frame = new JFrame("Chatroom Setup");
    frame.setSize(350,220);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(new EmptyBorder(15, 20,15,20));
    mainPanel.setBackground(Color.WHITE);
    //adding the labels
    //username input
    JLabel userNameLabel = new JLabel("Username: ");
    usernameField = new JTextField();

    JLabel portLabel = new JLabel("Server port: ");
    serverPortField = new JTextField();

    JLabel ipLabel = new JLabel("Server IP: ");
    ipAddressField = new JTextField("127.0.0.1");

    JButton connectionButton = new JButton("Connect");
    connectionButton.addActionListener(this::handleConnect);


    frame.add(userNameLabel);
    frame.add(usernameField);
    frame.add(portLabel);
    frame.add(serverPortField);
    frame.add(ipAddressField);
    frame.add(new JLabel());
    frame.add(connectionButton);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);
  }
  private void handleConnect(ActionEvent e) {
    String username = usernameField.getText().trim();
    String portText = serverPortField.getText().trim();
    String host = ipAddressField.getText().trim();

    if(username.isEmpty() || portText.isEmpty() || host.isEmpty()) {
      JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
      return;
    }
    try{
      int port = Integer.parseInt(portText);
      Client client = new Client(host, port);
      client.sendMessage(username);

      new Dashboard(username, client);
      frame.dispose();

    }catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(frame, "Invalid port number. ");
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(frame, "Unable to connect to the server.");
    }
  }
}