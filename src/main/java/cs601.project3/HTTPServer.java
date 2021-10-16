package cs601.project3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serial;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {

    private static volatile boolean running = true;


    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(running){

            try (
                Socket socket = serverSocket.accept();
                BufferedReader inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter outputStream = new PrintWriter(socket.getOutputStream(),true);
            ) {
                String header = "";
                String requestLine = inStream.readLine();
                String line = inStream.readLine();
                while(line != null && !line.trim().isEmpty()) {
                    header += line + "\n";
                    line = inStream.readLine();
                }
                System.out.println("requestLine: " + requestLine);
                System.out.println("header: \n" + header);

                String[] requestLineParts = requestLine.split("\\s+");

                String path = requestLineParts[1];
                if(!requestLineParts[0].equals("GET") && !requestLineParts[0].equals("POST")){
                    outputStream.println("HTTP/1.1 405");
                }
                outputStream.println("HTTP/1.1 200");
                outputStream.println("");

                String page = """
                               <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
                               <html xmlns="http://www.w3.org/1999/xhtml">
                                 <head>
                                   <title>Search Application</title>
                                 </head>
                                 <body>
                                  <form action="/reviewsearch" method="post">
                                    <input type="text" id="search" name="query" value="">
                                    <input type="submit" value="Search">
                                  </form>
                                 </body>
                               </html>
                               
                        """;
                        outputStream.println(page);

            }catch
             (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
