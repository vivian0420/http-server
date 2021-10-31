package cs601.project3;

/**
 * ChatApplication main class. Define port number, create HttpServer, setup path and handler and start the server.
 */
public class ChatApplication {
    public static void main(String[] args) {
        int port = 9090;
        HTTPServer server = new HTTPServer(port);
        server.addMapping("/slackbot", new ChatHandler());
        server.startup();
    }
}
