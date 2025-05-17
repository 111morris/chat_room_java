import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SplashScreen {
  private JFrame frame;
  private JTextField usernameField;
  private JTextField serverPortField;

  public SplashScreen(){
    frame = new JFrame("Chatroom Setup");
    frame.setSize(300,200);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new GridLayout(4,2,10,10));

    //adding the labels
    JLabel userNameLabel = new JLabel("Username: ");
    usernameField = new JTextField();

    JLabel portLabel = new JLabel("Server port: ");
    serverPortField = new JTextField();

    JButton connectionButton = new JButton("Connect");

    connectionButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        int port = Integer.parseInt(serverPortField.getText());

        //this will close the dashboard
        frame.dispose();

       //passing username and port to dashboard
        try{
          Client client = new Client("127.0.0.1", port);
          client.sendMessage(username);
          Dashboard dashboard = new Dashboard(username, client);
          dashboard.show();
        } catch (IOException ex) {
          JOptionPane.showMessageDialog(frame, "Unable to connect to server.");
        }

      }
    });

    frame.add(userNameLabel);
    frame.add(usernameField);
    frame.add(portLabel);
    frame.add(serverPortField);
    frame.add(new JLabel()); //Empty cell
    frame.add(connectionButton);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
  public static void main(String[] args) {

  }

}