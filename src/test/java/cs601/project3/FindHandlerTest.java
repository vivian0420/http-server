package cs601.project3;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindHandlerTest {

    private static FindHandler find;
    private static final Logger LOGGER = LogManager.getLogger(FindHandler.class.getName());
    private static JsonObject json;

    @BeforeAll
    public static void initializeHandler() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("config.json"), StandardCharsets.ISO_8859_1)) {
            json = new Gson().fromJson(br, JsonObject.class);
        } catch (IOException e) {
            LOGGER.error("Config can't be found, ", e);
        }
        Map<String, List<Amazon>> reviewAsinMap = new ReadFile(json.get("find").getAsJsonObject().get("review").getAsString()).readFile(AmazonReview.class);
        Map<String, List<Amazon>> qaAsinMap = new ReadFile(json.get("find").getAsJsonObject().get("qa").getAsString()).readFile(AmazonQA.class);

        find = new FindHandler(reviewAsinMap, qaAsinMap);
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
