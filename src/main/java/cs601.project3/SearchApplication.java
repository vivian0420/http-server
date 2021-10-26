package cs601.project3;

public class SearchApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        int port = 1024;
        HTTPServer server = new HTTPServer(port);
        //The request GET /reviewsearch will be dispatched to the
        //handle method of the ReviewSearchHandler.
        server.addMapping("/reviewsearch", new ReviewSearchHandler());
        //The request GET /find will be dispatched to the
        //handle method of the FindHandler.
        server.addMapping("/find", new FindHandler());
        server.startup();
    }

}
