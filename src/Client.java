import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

  public Client(String host, int port, String username) throws IOException{
    socket = new Socket(host, port);
    out = new PrintWriter(socket.getOutputStream(), true);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    //this will send the username explicitly
    out.println(username);
  }
  /**
   * Sends a message to the server
   * the first message send from the client (in SplashScreen) should be the nickname.
   * @param message
   */
  public void sendMessage(String message) {
    if(out !=null){
      out.println(message);
    }
  }

  
  public BufferedReader getIn() {
    return in;
  }
  public void close(){
    try {
      if(out != null) out.close();
      if(in != null) in.close();
      if(socket != null && !socket.isClosed()) socket.close();
    } catch (IOException e) {
      System.err.println("Error closing client connection: " + e.getMessage());
    }
  }
  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(SplashScreen::new);
  }
}
