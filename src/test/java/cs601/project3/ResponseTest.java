package cs601.project3;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResponseTest {

    @Test
    public void test200() {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        response.response("Hello Test.");
        assertTrue(writer.toString().contains("Hello Test."));
        assertTrue(writer.toString().startsWith("HTTP/1.1 200"));
    }

    @Test
    public void test400() {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        response.setCode(400);
        response.response("Hello Test.");
        assertTrue(writer.toString().contains("Hello Test."));
        assertTrue(writer.toString().startsWith("HTTP/1.1 400"));
        assertFalse(writer.toString().startsWith("HTTP/1.1 405"));
    }

    @Test
    public void test405() {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        ServerResponse response = new ServerResponse(out);
        response.setCode(405);
        response.response("Hello Test.");
        assertTrue(writer.toString().contains("Hello Test."));
        assertTrue(writer.toString().startsWith("HTTP/1.1 405"));
        assertFalse(writer.toString().startsWith("HTTP/1.1 400"));
    }

}
