import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class WebserverThread extends Thread {
    private static TemplateProcessor tp;
    private static LinkedDocumentCollection ldc;
    private Socket cliSock;

    public WebserverThread(TemplateProcessor tp, LinkedDocumentCollection ldc, Socket cliSock) {
        this.tp = tp;
        this.ldc = ldc;
        this.cliSock = cliSock;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cliSock.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(cliSock.getOutputStream()));
            try {
                communicate(in, out);
            } catch (IOException exp) {
            } finally {
                out.close();
                cliSock.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpResponse handleMainPage() {
        TreeMap<String, String> varass = new TreeMap<>();
        varass.put("%value", "");
        varass.put("%results", "");
        // replace() does not change TemplateProcessor at all.
        // It's thread-safe to invoke this method from multiple threads.
        String body = tp.replace(varass);
        return new HttpResponse(HttpStatus.Ok, body);
    }

    private HttpResponse handleSearchRequest(String query) {
        double pageRankDampingFactor = 0.85;
        double weightingFactor = 0.6;

        TreeMap<String, String> varass = new TreeMap<>();
        varass.put("%value", query);
        varass.put("%results", "");
        double[] relevance;
        String msg;
        // tp is shared by all threads, so we can use it as a synchronization object
        synchronized (tp) {
            // match() method changes ldc, so we should use synchronized() here
            relevance = ldc.match(query, pageRankDampingFactor, weightingFactor);
            // Though numDocuments() and get(i) does not change ldc,
            // we should understand that their return values depends on
            // when match()-method was invoked.
            // So, invocation of these two methods after match()-method should be atomically.
            // That's why we don't close synchronized() brace here.
            msg = "";
            if (ldc.numDocuments() > 0)
                msg += "<tr><td><b>ID</b></td><td><b>Page</b></td><td><b>Relevance</b></td></tr>";
            msg += IntStream.range(0, ldc.numDocuments()).boxed().reduce("", (acc, i) -> {
                acc += "<tr>";
                acc += "<td>" + (i + 1) + "</td>";
                acc += "<td><a href=\"" + ldc.get(i).getTitle() + "\">" + ldc.get(i).getTitle() + "</a></td>";
                acc += "<td>" + relevance[i] + "</td>";
                acc += "</tr>";
                return acc;
            }, (a, b) -> a + b);
        }
        varass.put("%results", msg);
        // replace() does not change TemplateProcessor at all.
        // It's thread-safe to invoke this method from multiple threads.
        String body = tp.replace(varass);
        return new HttpResponse(HttpStatus.Ok, body);
    }

    private HttpResponse handleFileRequest(String fileName) {
        // It's thread-safe to read one file by multiple threads,
        // if no threads trying to write to this file.
        if (fileName.contains("/"))
            return new HttpResponse(HttpStatus.Forbidden);
        String[] fileContents = Terminal.readFile(fileName);
        if (fileContents == null)
            return new HttpResponse(HttpStatus.NotFound);
        if(fileContents.length != 2)
            return new HttpResponse(HttpStatus.Forbidden);
        String body = fileContents[1];
        return new HttpResponse(HttpStatus.Ok, body);
    }

    private void communicate(BufferedReader in, PrintWriter out) throws IOException {
        /*
         * Todo: Request und Response-Klassen
         */

        String requestLine = in.readLine();
        if (requestLine == null)
            return;
        in.lines().takeWhile(l -> !l.equals("")).count();

        System.out.println("=> Request header received");

        HttpRequest request;
        try {
            request = new HttpRequest(requestLine);
        } catch (InvalidRequestException ex) {
            System.out.println("=> Bad request!");
            out.print(new HttpResponse(HttpStatus.BadRequest));
            return;
        }

        if (request.getMethod() != HttpMethod.GET) {
            System.out.println("=> Invalid method!");
            out.print(new HttpResponse(HttpStatus.MethodNotAllowed));
            return;
        }

        HttpResponse response;
        if (request.getPath().equals("/")) {
            System.out.println("=> Query for main page");
            response = handleMainPage();
        } else if (request.getPath().equals("/search")) {
            System.out.println("=> Search query");
            String query = request.getParameters().get("query");
            response = handleSearchRequest(query);
        } else {
            System.out.println("=> File query");
            String fileName = request.getPath().substring(1);
            response = handleFileRequest(fileName);
        }
        out.print(response);
    }
}
