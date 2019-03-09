import java.io.*;
import java.net.*;
import java.util.*;

public class WebserverThread extends Thread {
private static TemplateProcessor tp;
private static LinkedDocumentCollection ldc; 
private Socket cliSock;

public WebserverThread(TemplateProcessor tp, LinkedDocumentCollection ldc, Socket cliSock){
this.tp = tp;
this.ldc = ldc; 
this.cliSock = cliSock;
}

@Override 
public void run(){
try {
BufferedReader in = new BufferedReader (new InputStreamReader(cliSock.getInputStream()));
PrintWriter out = new PrintWriter(new OutputStreamWriter(cliSock.getOutputStream()));
try{
communicate(in, out); 
} catch (IOException exp){ 
} finally {
out.close(); 
cliSock.close();
}
} catch (IOException e){
e.printStackTrace();
}
}

private HttpResponse handleMainPage() {
TreeMap<String, String> varass = new TreeMap<>();
varass.put("%value", "");
varass.put("%results, "");
String body = tp.replace(varass); 
return new HttpResponse(HttpStatus.Ok, body); 
}

private HttpResponse handleSearchReauest(String query) {
double dampingFactor = 0.85; 
double weightingFactor = 0.6;

TreeMap<String, String> varass= new TreeMap<>();
varass.put("%value", query);
varass.put("%results", "");
double[] relevance; 
String msg; 

synchronized(tp){
relevance = ldc.match(query, dampingFactor, weightingFactor); 
msg = ""; 
if (ldc.numDocuments() > 0){
msg += "<tr><td><b>ID</b></td><td><b>Page</b></td><td><b>Relevance</td></b></tr>"; 
msg += IntStream.range(0, ldc.numDocuments()).boxed().reduce("", (acc, i) ->{
acc += "<tr>";
acc += "<td>" + (i+l) + "</td>";
acc += "<td><a href=\"" + ldc.get(i).getTitle() + "\">" + ldc.get(i).getTitle() + "</a></td>";
acc += "<td>" + relevance[i] + "</td>";
acc += "</tr>";
return acc;
}, (a,b) -> a+b);
}
varass.put("%results", msg);
String body = tp.replace(varass);
return new HttpResponse(HttpStatus.Ok, body); 
}
}
}
