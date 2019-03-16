import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class TestItServer {

  public static void main(String[] args) throws IOException {
    SynchronizedLdcWrapper ldcw = new SynchronizedLdcWrapper();
    @SuppressWarnings("resource")
    ServerSocket serverSocket = new ServerSocket(8000);
    while (true) {
      java.net.Socket client = serverSocket.accept();
      System.out.println("Client connected!");
      TestItThread thr = new TestItThread(ldcw, client);
      thr.start();
    }
  }

}
