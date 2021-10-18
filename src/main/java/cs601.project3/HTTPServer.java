package cs601.project3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HTTPServer {

    private static volatile boolean running = true;
    private Map<String, Handler> mapping;
    private ServerSocket serverSocket;
    private int port;

    public HTTPServer(int port) {
        this.port = port;
        this.mapping = new HashMap<>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMapping(String path, Handler handler) {

        this.mapping.put(path, handler);

    }


    public void startup() {

        new Thread(() -> {
            while (running) {
                Socket socket;
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                Map<String, String> headers = new HashMap<>();
                new Thread(() -> {
                    try (BufferedReader inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);) {

                        String requestLine = inStream.readLine();
                        String line = inStream.readLine();
                        while (line != null && !line.trim().isEmpty()) {
                            if (line.contains(":")) {
                                headers.put(line.split(":")[0].toLowerCase(), line.split(":")[1]);
                            }
                            line = inStream.readLine();
                        }

                        ServerRequest request = new ServerRequest(requestLine, headers, getContent(headers, inStream));
                        ServerResponse response = new ServerResponse(outputStream);
                        handleRequest(request, response);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }).start();
            }
        }).start();

    }

    public String getContent(Map<String, String> headers, BufferedReader in) {
        if (!headers.containsKey("content-length")) {
            return null;
        }
        int length = Integer.parseInt(headers.get("content-length").trim());
        char[] content = new char[length];
        try {
            in.read(content, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new String(content);
    }

    public void handleRequest(ServerRequest request, ServerResponse response) {
        if (request.is400()) {
            response.setCode(400);
            response.response("400 Bad Request");
        } else if (request.is405()) {
            response.setCode(405);
            response.response("405 Method Not Allowed");
        } else if (!this.mapping.containsKey(request.getPath())) {
            response.setCode(404);
            response.response("404 Not Found");
        } else {
            this.mapping.get(request.getPath()).handle(request, response);
        }

    }

}
