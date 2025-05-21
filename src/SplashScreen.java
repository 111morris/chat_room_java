import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
    JLabel userNameLabel = new JLabel("Username:");
    userNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));


    // username input
    usernameField = new JTextField();
    usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    addPlacholder(usernameField, "Enter username");
    usernameField.setToolTipText("Type your chat username here");

     //server port input
    serverPortField = new JTextField();
    serverPortField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    addPlacholder(serverPortField, "e.g. 9999");
    serverPortField.setToolTipText("Enter the server port number");
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    formPanel.add(serverPortField, gbc);

    mainPanel.add(formPanel, BorderLayout.CENTER);

    //the connect button
    JButton connectButton = new JButton("Connect");
    connectButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
    connectButton.setBackground(new Color(33,150,243));
    connectButton.setForeground(Color.WHITE);
    connectButton.setFocusPainted(false);
    connectButton.setBorder(BorderFactory.createEmptyBorder(8,20,8,20));
    connectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    connectButton.addActionListener(this::handleConnect);

    //adding the hover effect
    connectButton.addMouseListener(new java.awt.event.MouseAdapter(){
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        connectButton.setBackground(new Color(30, 136,229));
      }
      public void  mouseExited(java.awt.event.MouseEvent evt) {
        connectButton.setBackground(new Color(33,150, 243));
      }
    });

    // panel for the button
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setBorder(new EmptyBorder(15,0,0,0));
    buttonPanel.add(connectButton);

    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    frame.setContentPane(mainPanel);
    frame.setVisible(true);






    //JLabel portLabel = new JLabel("Server port: ");
    //serverPortField = new JTextField();

    //JLabel ipLabel = new JLabel("Server IP: ");
    //ipAddressField = new JTextField("127.0.0.1");

    //connectionButton.addActionListener(this::handleConnect);

    //frame.add(userNameLabel);
    //frame.add(usernameField);
    //frame.add(portLabel);
    //frame.add(serverPortField);
    //frame.add(ipAddressField);
    //frame.add(new JLabel());
    //frame.add(connectionButton);
    //frame.setLocationRelativeTo(null);
    //frame.setResizable(false);
    //frame.setVisible(true);
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