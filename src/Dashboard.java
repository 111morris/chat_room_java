import javax.swing.*;

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
    panel.setLayout(new BoxLayout());
  }
  public void show() {
    frame.setVisible(true);
  }
}
