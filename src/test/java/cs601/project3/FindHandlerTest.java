package cs601.project3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindHandlerTest {

    private static FindHandler find;
    @BeforeAll
    public static void initializeHandler() {
        find = new FindHandler();
    }

    @Test
    public void testHandleValidGet() {
        ServerRequest request = new ServerRequest("GET /find HTTP/1.1", null, "");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        find.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 200"));
        assertTrue(writer.toString().contains("content-length"));
        assertTrue(writer.toString().contains("!DOCTYPE html PUBLIC"));
    }

    @Test
    public void testHandleValidPost() {
        ServerRequest request = new ServerRequest("POST /find HTTP/1.1", null, "asin=3998899561");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        find.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 200"));
        assertTrue(writer.toString().contains("content-length"));
        assertTrue(writer.toString().contains("!DOCTYPE html PUBLIC"));
        assertTrue(writer.toString().contains("<title>find Application</title>"));
        assertTrue(writer.toString().toLowerCase().contains("3998899561"));
    }

    @Test
    public void testHandleNotFound() {
        ServerRequest request = new ServerRequest("POST /find HTTP/1.1", null, "asin=399889956");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        find.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 200"));
        assertTrue(writer.toString().contains("content-length"));
        assertTrue(writer.toString().contains("!DOCTYPE html PUBLIC"));
        assertTrue(writer.toString().contains("<title>find Application</title>"));
        assertFalse(writer.toString().toLowerCase().contains("399889956"));
        assertTrue(writer.toString().contains("0 result showed"));
    }

    @Test
    public void testHandleEmptyTerm() {
        ServerRequest request = new ServerRequest("POST /find HTTP/1.1", null, "asin=");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        find.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 200"));
        assertTrue(writer.toString().contains("content-length"));
        assertTrue(writer.toString().contains("!DOCTYPE html PUBLIC"));
        assertTrue(writer.toString().contains("<title>find Application</title>"));
        assertTrue(writer.toString().contains("Please enter"));
    }

    @Test
    public void testHandleBadRequest() {
        ServerRequest request = new ServerRequest("POST /find HTTP/1.1", null, "asin1=3998899561");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        find.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 400"));
        assertTrue(writer.toString().contains("400 Bad Request"));
    }
}
