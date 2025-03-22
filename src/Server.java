import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{

  private ArrayList<ConnectionHandler> connections;
  @Override
  public void run(){
    try{
      ServerSocket server = new ServerSocket(9999);
      Socket client = server.accept();
      ConnectionHandler handler = new ConnectionHandler(client);
      connections.add(handler);

    } catch(IOException e){
      // todo: handle

    }
  }
  class ConnectionHandler implements Runnable{

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String nickname;

    //constructor
    public ConnectionHandler(Socket client) {
      this.client = client;
    }

    @Override
    public void run() {
      try {
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new inputStreamReader(client.getInputStream()));
        out.println("Please input your nick name: ");
        nickname = in.readLine();

        System.out.println(nickname+"connected!");
      } catch (IOException e){
        //todo:handle
      }
    }
    public void sendMessage(String message){
      out.println(message);
    }
  }

}
