import javax.swing.*;
import javax.swing.border.Border;
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

  public SplashScreen(){
    frame = new JFrame("Chatroom Setup");
    frame.setSize(300,200);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new GridLayout(4,2,10,10));

    // username label
    JLabel userNameLabel = new JLabel("Username: ");
    userNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

    // username input
    usernameField = new JTextField();
    usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    addPlacholder(usernameField, "Enter username");
    usernameField.setToolTipText("Type your chat username here");

     //server port label and input
    JLabel portLabel = new JLabel("Server port: ");
    portLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    serverPortField = new JTextField();

    serverPortField = new JTextField();
    serverPortField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    addPlacholder(serverPortField, "e.g. 9999");
    serverPortField.setToolTipText("Enter the server port number");

    //sever ip Label and input
    JLabel ipLabel = new JLabel("Server IP: ");
    ipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    ipAddressField = new JTextField();
    ipAddressField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    addPlacholder(ipAddressField, "127.0.0.1");
    ipAddressField.setToolTipText("Enter the server IP address");

    //the connect button
    JButton connectButton = new JButton("Connect");
    connectButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
    connectButton.setBackground(new Color(33,150,243));
    connectButton.setForeground(Color.WHITE);
    connectButton.setFocusPainted(false);
//    connectButton.setBorder(BorderFactory.createEmptyBorder(8,20,8,20));
    connectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    connectButton.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {

      }

      @Override
      public void mousePressed(MouseEvent e) {

      }

      @Override
      public void mouseReleased(MouseEvent e) {

      }

      @Override
      public void mouseEntered(MouseEvent e) {
        connectButton.setBackground(new Color(30,136,229));
      }

      @Override
      public void mouseExited(MouseEvent e) {
        connectButton.setBackground(new Color(33,150,243));
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

    frame.add(userNameLabel);
    frame.add(usernameField);
    frame.add(portLabel);
    frame.add(serverPortField);
    frame.add(ipLabel);
    frame.add(ipAddressField);
    frame.add(new JLabel());
    frame.add(connectButton);
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

    if(username.isEmpty() || username.equals("Enter username") ||
        portText.isEmpty() || portText.equals("e.g. 9999") ||
        host.isEmpty() || host.equals("127.0.0.1")) {
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
  public static void main(String[] args) {
    SwingUtilities.invokeLater(SplashScreen::new);
  }
}