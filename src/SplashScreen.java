import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        //pass values to server and client

        Server server = new Server(port);
        server.run();
      }
    });

    frame.add(userNameLabel);
    frame.add(usernameField);
    frame.add(portLabel);
    frame.add(serverPortField);
    frame.add(new JLabel()); //Empty cell
    frame.add(connectionButton);

    frame.setLocationRelativeTo(null);

    frame.setVisible(true);
  }

}