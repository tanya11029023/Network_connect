import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class Webserver {
  public static void main(String[] args) throws IOException {
    // Threads share tp and temp
    TemplateProcessor tp = new TemplateProcessor("html/search.html");
    LinkedDocumentCollection temp = new LinkedDocumentCollection();
    temp.appendDocument(new LinkedDocument("B.txt", "", "", null, null, "link:A.txt link:E.txt", "B.txt"));
    // documents crawling
    LinkedDocumentCollection ldc = temp.crawl();
    @SuppressWarnings("resource")
    ServerSocket serverSocket = new ServerSocket(8000);
    IntStream.iterate(0, i -> i + 1).forEach(i -> {
      Socket client;
      try {
        client = serverSocket.accept();
        System.out.println("*** Client connected!");
        WebserverThread thread = new WebserverThread(tp, ldc, client);
        thread.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

}
