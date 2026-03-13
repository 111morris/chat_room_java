import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class SplashScreen {
  private JFrame frame;
  private JTextField usernameField;
  private JTextField serverPortField;
  private JTextField ipAddressField;

  private final Color placeholderColor = Color.GRAY;
  private final Color textColor = Color.BLACK;

  public JTextField getUsernameField() {
    return usernameField;
  }

  public JTextField getServerPortField() {
    return serverPortField;
  }

  public JTextField getIpAddressField() {
    return ipAddressField;
  }

  public SplashScreen(){
    frame = new JFrame("Chatroom Setup");
    frame.setSize(400,320);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // main panel with padding
    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
    mainPanel.setBackground(new Color(245, 247, 250)); // modern light gray/blue background

    //title label
    JLabel titleLabel = new JLabel("Join Chatroom");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
    titleLabel.setForeground(new Color(44, 62, 80)); // dark elegant text
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(new EmptyBorder(0,0,20,0));
    mainPanel.add(titleLabel, BorderLayout.NORTH);

    //form panel
    JPanel formPanel = new JPanel(new GridLayout(4,2,10,15));
    formPanel.setBackground(mainPanel.getBackground());
    formPanel.setOpaque(false);

    // username label
    JLabel userNameLabel = new JLabel("Username: ");
    userNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    userNameLabel.setForeground(new Color(52, 73, 94));
    // username input
    usernameField = new ModernTextField();
    usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    addPlacholder(usernameField, "Enter username");
    usernameField.setToolTipText("Type your chat username here");

     //server port label and input
    JLabel portLabel = new JLabel("Server Port: ");
    portLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    portLabel.setForeground(new Color(52, 73, 94));

    serverPortField = new ModernTextField();
    serverPortField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    addPlacholder(serverPortField, "e.g. 9999");
    serverPortField.setToolTipText("Enter the server port number");

    //sever ip Label and input
    JLabel ipLabel = new JLabel("Server IP: ");
    ipLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    ipLabel.setForeground(new Color(52, 73, 94));
    ipAddressField = new ModernTextField();
    ipAddressField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    addPlacholder(ipAddressField, "127.0.0.1");
    ipAddressField.setToolTipText("Enter the server IP address");

    //the connect button
    ModernButton connectButton = new ModernButton("Connect");
    connectButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
    connectButton.setBackground(new Color(41, 128, 185)); // modern blue
    connectButton.setForeground(Color.WHITE);
    connectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // Modern hover effect
    connectButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        connectButton.setBackground(new Color(52, 152, 219)); // lighter blue on hover
      }

      @Override
      public void mouseExited(MouseEvent e) {
        connectButton.setBackground(new Color(41, 128, 185));
      }
    });

    connectButton.addActionListener(this::handleConnect);
/*
    //adding the hover effect
    connectButton.addMouseListener(new java.awt.event.MouseAdapter(){
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        connectButton.setBackground(new Color(30, 136,229));
      }
      public void  mouseExited(java.awt.event.MouseEvent evt) {
        connectButton.setBackground(new Color(33,150, 243));
      }
    });
    */

    //JLabel portLabel = new JLabel("Server port: ");
    //serverPortField = new JTextField();

    //JLabel ipLabel = new JLabel("Server IP: ");
    //ipAddressField = new JTextField("127.0.0.1");

    //connectionButton.addActionListener(this::handleConnect);
    /*
    frame.add(userNameLabel);
    frame.add(usernameField);
    frame.add(portLabel);
    frame.add(serverPortField);
    frame.add(ipLabel);
    frame.add(ipAddressField);
    frame.add(new JLabel());
    frame.add(connectButton);
    frame.setVisible(true);
    */
    formPanel.add(userNameLabel);
    formPanel.add(usernameField);
    formPanel.add(portLabel);
    formPanel.add(serverPortField);
    formPanel.add(ipLabel);
    formPanel.add(ipAddressField);
    formPanel.add(new JLabel()); // empty cell
    formPanel.add(connectButton);

    mainPanel.add(formPanel, BorderLayout.CENTER);
    frame.setContentPane(mainPanel);
    frame.setVisible(true);
  }
  private void addPlacholder(JTextField field, String placeholder) {
    field.setForeground(placeholderColor);
    field.setText(placeholder);
    field.setFont(field.getFont().deriveFont(Font.ITALIC));
    field.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        if(field.getText().equals(placeholder)){
          field.setText("");
          field.setForeground(textColor);
          field.setFont(field.getFont().deriveFont(Font.PLAIN));

        }
      }
      @Override
      public void focusLost(FocusEvent e) {
        if(field.getText().isEmpty()) {
          field.setForeground(placeholderColor);
          field.setText(placeholder);
          field.setFont(field.getFont().deriveFont(Font.ITALIC));

        }
      }
    });
  }

  private void handleConnect(ActionEvent e) {
    String username = usernameField.getText().trim();
    String portText = serverPortField.getText().trim();
    String host = ipAddressField.getText().trim();

    // this will check if placeholder is still present or fields empty
    if(username.isEmpty() || username.equals("Enter username") ||
        portText.isEmpty() || portText.equals("e.g. 9999") ||
        host.isEmpty()) {
      JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
      return;
    }
    try{
      int port = Integer.parseInt(portText);
      Client client = new Client(host, port, username);
      client.sendMessage(username);

      new Dashboard(username, client);
      frame.dispose();

    }catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(frame, "Invalid port number. ");
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(frame, "Unable to connect to the server.");
    }
  }
  public static void main(String[] args) {
    //SwingUtilities.invokeLater(SplashScreen::new);
    SwingUtilities.invokeLater(SplashScreen::new);
  }
}