package cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;

  public Client(String host, int port) throws IOException {
    socket = new Socket(host, port);
    out = new PrintWriter(socket.getOutputStream(), true);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  public BufferedReader getIn() {
    return in;
  }
  public void close(){
    if(out !=null) out.close();
  }
}