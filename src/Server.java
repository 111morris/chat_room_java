import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;

public class Server implements Runnable {

  private ArrayList<ConnectionHandler> connections;
  private ServerSocket server;
  private boolean done;
  private ExecutorService pool;
  private int port;


  public Server(int port) {
    this.port = port;
    connections = new ArrayList<>();
    done = false;
  }

  @Override
  public void run() {
    try {
      //serverSocket is 9999
      server = new ServerSocket(port);
      pool = Executors.newCachedThreadPool();
      while (!done) {
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

  public void broadcast(String message) {
    for (ConnectionHandler ch : connections) {
      if (ch != null) {
        ch.sendMessage(message);
      }
    }
  }

  public void shutdown() {
    try {
      done = true;
      pool.shutdown();
      if (!server.isClosed()) {
        server.close();
      }
      for (ConnectionHandler ch : connections) {
        ch.shutdown();
      }
    } catch (IOException e) {
      // ignore
    }
  }

  // Generate a random color
  private String getRandomColor() {
    String[] colors = {"\033[31m", "\033[32m", "\033[33m", "\033[34m", "\033[35m"};
    Random rand = new Random();
    return colors[rand.nextInt(colors.length)];
  }

  class ConnectionHandler implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String nickname;
    private String color;  // Store the color for this client

    public ConnectionHandler(Socket client) {
      this.client = client;
      this.color = getRandomColor();  // Assign random color when a client connects
    }

    @Override
    public void run() {
      try {
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        out.println("Please input your nickname: ");
        nickname = in.readLine();
        System.out.println(nickname + " connected!");

        // Broadcast that the user has joined
        broadcast(color + nickname + " joined the chat!" + "\033[0m");

        String message;
        while ((message = in.readLine()) != null) {
          if (message.startsWith("/quit") || message.startsWith("/EXIT")) {
            broadcast(color + nickname + " left the chat!" + "\033[0m");
            shutdown();
          } else if (message.startsWith("/nick ")) {
            String[] messageSplit = message.split(" ", 2);
            if (messageSplit.length == 2) {
              broadcast(color + nickname + " changed their name to " + messageSplit[1] + "\033[0m");
              nickname = messageSplit[1];
              out.println("Successfully changed nickname to " + nickname);
            } else {
              out.println("No nickname provided!");
            }
          } else {
            broadcast(color + nickname + ": " + message + "\033[0m");
          }
        }
      } catch (IOException e) {
        shutdown();
      }
    }

    public void sendMessage(String message) {
      out.println(message);
    }

    public void shutdown() {
      try {
        in.close();
        out.close();
        if (!client.isClosed()) {
          client.close();
        }
      } catch (IOException e) {
        // ignore
      }
    }
  }

  public static void main(String[] args) {


  }
}
