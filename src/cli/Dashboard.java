package cli;

import javax.swing.*;

public class Dashboard extends JFrame{
  private  String username;
  private Client client;
  public Dashboard(String username, Client client) {
    this.username = username;
    this.client = client;


  }
}