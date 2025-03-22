import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Client implements Runnable {
  private Socket client;
  private BufferedReader in;
  private PrintWriter out;
  private boolean done;
  private String clientColor;

  // Random color generator for clients
  private static final String[] COLORS = {"\033[31m", "\033[32m", "\033[33m", "\033[34m", "\033[35m"};

  public Client() {
    // Randomly assign a color
    Random random = new Random();
    this.clientColor = COLORS[random.nextInt(COLORS.length)];
  }

  @Override
  public void run() {
    try {
      client = new Socket("127.0.0.1", 9999);
      out = new PrintWriter(client.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(client.getInputStream()));

      InputHandler inHandler = new InputHandler();
      Thread t = new Thread(inHandler);
      t.start();

      String inMessage;
      while ((inMessage = in.readLine()) != null) {
        System.out.println(clientColor + inMessage + "\033[0m");  // Print the message in client color
      }
    } catch (IOException e) {
      shutdown();
    }
  }

  public void shutdown() {
    done = true;
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

  class InputHandler implements Runnable {
    @Override
    public void run() {
      try {
        BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
        while (!done) {
          String message = inReader.readLine();
          if (message.equals("/quit") || message.equals("/QUIT") || message.equals("/exit") || message.equals("/EXIT")) {
            out.println(message);
            inReader.close();
            shutdown();
          } else {
            out.println(message);
          }
        }
      } catch (IOException e) {
        shutdown();
      }
    }
  }

  public static void main(String[] args) {
    Client client = new Client();
    client.run();
  }
}
