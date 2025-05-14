public class Main {
  public static void main(String[] args) {
    Server server = new Server();

    Dashboard dashboard = new Dashboard();
    dashboard.show();
    server.run();
  }
}