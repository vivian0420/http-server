package cs601.project3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * HTTPServer class
 */
public class HTTPServer {
    private static final Logger LOGGER = LogManager.getLogger(HTTPServer.class.getName());
    final private ExecutorService executorService;
    private volatile boolean running;
    private Map<String, Handler> mapping;
    private ServerSocket serverSocket;
    private int port;

    /**
     * Constructor. Specify the the number of port and create serverSocket and a HashMap of mapping.
     *
     * @param port the number of port that we will listen to
     */
    public HTTPServer(int port) {
        this.port = port;
        this.mapping = new HashMap<>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.running = true;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    /**
     * Add request path and handler to mapping
     *
     * @param path    request path
     * @param handler instance of Handler
     */
    public void addMapping(String path, Handler handler) {

        this.mapping.put(path, handler);

    }

    /**
     * Start the server to serve HTTP requests.
     */
    public void startup() {
        // start a new thread to unblock the main function
        new Thread(() -> {
            while (running) {
                Socket socket;
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    LOGGER.warn("Can't accept client socket.");
                    continue;
                }
                // start a new thread to handle each socket so that "Each incoming request will be handled by a different thread."
                this.executorService.execute(() -> {
                    try (PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
                         BufferedReader inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                    ) {
                        Map<String, String> headers = new HashMap<>();
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
                        if (request.getRequestMethod() != null && request.getRequestMethod().equals("POST") &&
                                request.getPath() != null && request.getPath().equals("/shutdown")) {
                            this.shutDown();
                            return;
                        }
                        handleRequest(request, response);
                    } catch (IOException e) {
                        LOGGER.error("Error handing HTTP request.", e);
                    }
                });
            }
        }).start();
        LOGGER.info("Server started at http://localhost:" + this.port);
    }

    /**
     * Get HTTP post content
     *
     * @param headers HTTP header
     * @param in      BufferedReader
     * @return post content
     */
    private String getContent(Map<String, String> headers, BufferedReader in) {
        if (!headers.containsKey("content-length")) {
            return null;
        }
        int length = Integer.parseInt(headers.get("content-length").trim());
        char[] content = new char[length];
        try {
            in.read(content, 0, length);
        } catch (IOException e) {
            LOGGER.error("Couldn't get the content", e);
            return null;
        }
        return new String(content);
    }

    /**
     * Handle HTTP requests
     *
     * @param request  The HTTP request that the server receives
     * @param response The HTTP response that the server sends
     */
    public void handleRequest(ServerRequest request, ServerResponse response) {
        if (request.is400()) {
            response.setCode(400);
            response.response("<html xmlns=\"http://www.w3.org/1999/xhtml\">400 Bad Request</html>");
        } else if (request.is405()) {
            response.setCode(405);
            response.response("<html xmlns=\"http://www.w3.org/1999/xhtml\">405 Method Not Allowed</html>");
        } else if (!this.mapping.containsKey(request.getPath())) {
            response.setCode(404);
            response.response("<html xmlns=\"http://www.w3.org/1999/xhtml\">404 Not Found</html>");
        } else {
            try {
                this.mapping.get(request.getPath()).handle(request, response);
            } catch (Exception e) {
                LOGGER.error("500 Internal Server Error", e);
                response.setCode(500);
                response.response("<html xmlns=\"http://www.w3.org/1999/xhtml\">500 Internal Server Error</html>");
            }
        }
    }

    /**
     * Shutdown method. set up running as false and close the server socket.
     */
    public void shutDown() {
        this.running = false;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            LOGGER.error("Unable to close the server socket.", e);
        }
        System.exit(0);
    }

}
