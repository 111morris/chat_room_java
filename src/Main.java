public class Main {
  public static void main(String[] args) {
    Server server = new Server();
    server.run();

    Dashboard dashboard = new Dashboard();
    dashboard.show();
  }
}