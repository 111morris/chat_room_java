public class Main {
  public static void main(String[] args) {
    //the server starts automatic
    new Thread(() -> {
      Server server = new Server(9999);
      server.run();
    }).start();
  }
}