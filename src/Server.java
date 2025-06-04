import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements Runnable {

  private List<ConnectionHandler> connections;
  private ServerSocket server;
  private ExecutorService pool;
  private boolean isRunning;
  private int port;

  public Server(int port) {
    this.port = port;
    this.connections = new CopyOnWriteArrayList<>();
    this.isRunning = true;
  }

  @Override
  public void run() {
    try {
      //serverSocket is 9999
      server = new ServerSocket(port);
      pool = Executors.newCachedThreadPool();
      while (isRunning) {
        Socket client = server.accept();
        ConnectionHandler handler = new ConnectionHandler(client);
        connections.add(handler);
        pool.execute(handler);
      }
    } catch (IOException e) {
      shutdown();
      System.out.println("system closed");
    }
  }

  public void broadcast(String message, ConnectionHandler sender) {
    for (ConnectionHandler ch : connections) {
      if (ch != sender) {
        ch.sendMessage(message);
      }
    }
  }
  public void shutdown() {
    isRunning = false;
    try {
      if(server != null && !server.isClosed()){
        server.close();
      }
      for(ConnectionHandler ch: connections) {
        ch.shutdown();
      }
      if (pool != null && !pool.isShutdown()) {
        pool.shutdown();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  private class ConnectionHandler implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String nickname;

    public ConnectionHandler(Socket client) {
      this.client = client;
    }

    @Override
    public void run() {
      try {
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        nickname = in.readLine();
        System.out.println(nickname + " connected.");
        broadcast("[" + getCurrentTime() + "] " + nickname + " joined the chat!", this);
        String message;
        while ((message = in.readLine()) != null) {
          if (message.equalsIgnoreCase("/quit") || message.equalsIgnoreCase("/exit")) {
            broadcast("[" + getCurrentTime() + "] " + nickname + " left the chat.", this);
            break;
          } else if (message.startsWith("/nick ")) {
            String[] split = message.split(" ", 2);
            if (split.length == 2) {
              String oldName = nickname;
              nickname = split[1];
              out.println("[" + getCurrentTime() + "] Nickname changed to " + nickname);
              broadcast("[" + getCurrentTime() + "] " + oldName + " is now known as " + nickname, this);
            } else {
              out.println("Usage: /nick newname");
            }
          } else {
            broadcast("[" + getCurrentTime() + "] " + nickname + ": " + message, this);
          }
        }
      } catch (IOException e) {
        System.out.println("Connection error with " + nickname);
      } finally {
        shutdown();
      }
    }

    private String getCurrentTime() {
      java.time.LocalTime now = java.time.LocalTime.now();
      java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a");
      return now.format(formatter);
    }
    public void sendMessage(String message) {
      out.println(message);
    }
    public void shutdown() {
      try {
        if(in != null) in.close();
        if(out != null) out.close();
        if(client != null && !client.isClosed()) client.close();;
        connections.remove(this);
      } catch (IOException e) {
        // ignore this
      }
    }
  }
  public static void main(String[] args) {
    int port = 9999;
    Server server = new Server(port);
    Thread thread = new Thread(server);
    thread.start();
  }
}