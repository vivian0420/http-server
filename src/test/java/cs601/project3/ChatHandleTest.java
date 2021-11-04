package cs601.project3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatHandleTest {

    private static ChatHandler chat;

    @BeforeAll
    public static void initializeHandler() {
        chat = new ChatHandler();
    }

    @Test
    public void testHandleGet() {
        ServerRequest request = new ServerRequest("GET /slackbot HTTP/1.1", null, "");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        chat.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 200"));
        assertTrue(writer.toString().contains("content-length"));
        assertTrue(writer.toString().contains("!DOCTYPE html PUBLIC"));
        assertTrue(writer.toString().contains("slackbot"));
    }

    @Test
    public void testHandleValidPost() {
        ServerRequest request = new ServerRequest("POST /slackbot HTTP/1.1", null, "message=Hello World");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        chat.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 200"));
        assertTrue(writer.toString().contains("content-length"));
        assertTrue(writer.toString().contains("!DOCTYPE html PUBLIC"));
        assertTrue(writer.toString().contains("true"));

    }

    @Test
    public void testHandleEmptyPost() {
        ServerRequest request = new ServerRequest("POST /slackbot HTTP/1.1", null, "message=");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        chat.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 200"));
        assertTrue(writer.toString().contains("content-length"));
        assertTrue(writer.toString().contains("!DOCTYPE html PUBLIC"));
        assertTrue(writer.toString().contains("false"));

    }

    @Test
    public void testHandleBadRequestPost() {
        ServerRequest request = new ServerRequest("POST /slackbot HTTP/1.1", null, "message1=");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        chat.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 400"));
        assertTrue(writer.toString().contains("400 Bad Request"));
    }

    @Test
    public void testGetTerm() {
        ServerRequest request = new ServerRequest("POST /slackbot HTTP/1.1", null, "");
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        chat.handle(request, response);
        assertTrue(writer.toString().startsWith("HTTP/1.1 400"));
        assertTrue(writer.toString().contains("400 Bad Request"));
    }


}
