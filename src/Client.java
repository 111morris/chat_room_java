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
    out = new PrintWriter(socket.getOutputStream(), true);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  /**
   * Sends a message to the server
   * the first message send from the client (in SplashScreen) should be the nickname.
   * @param message
   */
  public void sendMessage(String message) {
    if(out !=null){
      
    }
  }
  public BufferedReader getIn() {
    return in;
  }
  public void close(){
    try {
      if(in != null) in.close();
      if(out != null) out.close();
      if(socket != null && !socket.isClosed()) socket.close();

    } catch (IOException e) {
      //ignore
      System.out.println("ignored");
    }
  }

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(SplashScreen::new);
  }

}