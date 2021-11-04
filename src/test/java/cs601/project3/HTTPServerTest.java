package cs601.project3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HTTPServerTest {


    private static HTTPServer server;
    @BeforeAll
    public static void startTheServer() {
        server = new HTTPServer(8088);
        server.addMapping("/find", new FindHandler());
        server.addMapping("/reviewSearch", new ReviewSearchHandler());
        server.startup();
    }

    @Test
    public void testHandleBadRequest1() {
        ServerRequest request = new ServerRequest("get /find HTTP", null, "");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        server.handleRequest(request, response);
        assertTrue(writer.toString().contains("400 Bad Request"));
    }

    @Test
    public void testHandleBadRequest2() {
        ServerRequest request = new ServerRequest("GET find HTTP", null, "");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        server.handleRequest(request, response);
        assertTrue(writer.toString().contains("400 Bad Request"));
    }

    @Test
    public void testHandleInvalidMethod() {
        ServerRequest request = new ServerRequest("DELETE /find HTTP", null, "");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        server.handleRequest(request, response);
        assertTrue(writer.toString().contains("405 Method Not Allowed"));
    }

    @Test
    public void testHandleInvalidPath() {
        ServerRequest request = new ServerRequest("POST /path HTTP", null, "");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        server.handleRequest(request, response);
        assertTrue(writer.toString().contains("404 Not Found"));
    }

    @Test
    public void testHandleMissingContent() {
        ServerRequest request = new ServerRequest("POST /find HTTP/1.1", null, "");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        server.handleRequest(request, response);
        assertTrue(writer.toString().contains("HTTP/1.1 400"));
    }

}
