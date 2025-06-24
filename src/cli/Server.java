package cli;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

public class Server implements Runnable{

}
  class ConnectionHandler implements Runnable{
   private Socket client;
   private BufferedReader in;
   private PrintWriter out;

    public ConnectionHandler(Socket client){
      this.client = client;
    }
    @Override
    public void run(){
      try {
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    public void broadcast(String message, ConnectionHandler sender){
  }
}