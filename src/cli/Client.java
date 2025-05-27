package cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

  public Client() {

  }
  public void sendMessage(String message) {
    if(out != null) {
      out.println(message);
    }
  }

  public BufferedReader getIn() {
    return in;
  }
  public void close(){
    try{
      if(out != null) out.close();
      if(in != null) in.close();
      if(socket != null && !socket.isClosed()) socket.close();
    } catch (IOException e) {
      System.err.println("Error closing client connection: " + e.getMessage());
    }
  }
  public void main(String[] args) {

  }
}
