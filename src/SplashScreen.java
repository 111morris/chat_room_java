import javax.swing.*;
import java.awt.*;

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


    frame.setVisible(true);
  }

}