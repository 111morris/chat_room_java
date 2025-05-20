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

    // title label
    JLabel titleLabel = new JLabel("Chatroom Setup", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
    titleLabel.setBorder(new EmptyBorder(0,0,15,0));
    mainPanel.add(titleLabel, BorderLayout.NORTH);

    // form panel using gridbaglayout for flexibility
    JPanel formPanel = new JPanel(new GridLayout());
    formPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(8,5,8,5);

    // username label
    JLabel userNameLabel = new JLabel("Username:");
    userNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    gbc.gridx = 0;
    gbc.gridy = 0;
    formPanel.add(userNameLabel, gbc);

    // username input
    usernameField = new JTextField();
    usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    addPlacholder(usernameField, "Enter username");
    usernameField.setToolTipText("Type your chat username here");
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    formPanel.add(usernameField, gbc);

    //server port input
    serverPortField = new JTextField();
    serverPortField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    addPlaceholder(serverPortField, "e.g. 9999");
    serverPortField.setToolTipText("Enter the server port number");
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    formPanel.add(serverPortField, gbc);

    mainPanel.add(formPanel, BorderLayout.CENTER);

    //the connect button


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