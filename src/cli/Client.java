package cli;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;
}