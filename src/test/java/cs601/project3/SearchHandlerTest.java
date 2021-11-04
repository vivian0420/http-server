package cs601.project3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchHandlerTest {

    private static ReviewSearchHandler search;
    @BeforeAll
    public static void initializeHandler() {
        search = new ReviewSearchHandler();
    }

    @Test
    public void testHandleValidGet() {
        ServerRequest request = new ServerRequest("GET /reviewsearch HTTP/1.1", null, "");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        search.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 200"));
        assertTrue(writer.toString().contains("content-length"));
        assertTrue(writer.toString().contains("!DOCTYPE html PUBLIC"));
    }

    @Test
    public void testHandleValidPost() {
        ServerRequest request = new ServerRequest("POST /reviewsearch HTTP/1.1", null, "query=apple");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        search.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 200"));
        assertTrue(writer.toString().contains("content-length"));
        assertTrue(writer.toString().contains("!DOCTYPE html PUBLIC"));
        assertTrue(writer.toString().contains("<title>reviewsearch Application</title>"));
        assertTrue(writer.toString().toLowerCase().contains("apple"));
    }

    @Test
    public void testHandleNotFoundPost() {
        ServerRequest request = new ServerRequest("POST /reviewsearch HTTP/1.1", null, "query=appl");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        search.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 200"));
        assertTrue(writer.toString().contains("content-length"));
        assertTrue(writer.toString().contains("!DOCTYPE html PUBLIC"));
        assertTrue(writer.toString().contains("<title>reviewsearch Application</title>"));
        assertTrue(writer.toString().contains("is not found"));
    }

    @Test
    public void testHandleEmptySearch() {
        ServerRequest request = new ServerRequest("POST /reviewsearch HTTP/1.1", null, "query=");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        search.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 200"));
        assertTrue(writer.toString().contains("content-length"));
        assertTrue(writer.toString().contains("!DOCTYPE html PUBLIC"));
        assertTrue(writer.toString().contains("<title>reviewsearch Application</title>"));
        assertTrue(writer.toString().contains("Please enter"));
    }

    @Test
    public void testHandleBadRequest() {
        ServerRequest request = new ServerRequest("POST /reviewsearch HTTP/1.1", null, "query1=");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        search.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 400"));
        assertTrue(writer.toString().contains("400 Bad Request"));
    }
}
