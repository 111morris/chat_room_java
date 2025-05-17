import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

  public Client(String host, int port) throws IOException{
    socket = new Socket(host, port);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
  }


  public static void main(String[] args) {

  }
}