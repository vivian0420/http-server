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

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchHandlerTest {
    private static final Logger LOGGER = LogManager.getLogger(FindHandler.class.getName());
    private static JsonObject json;
    private static ReviewSearchHandler search;
    @BeforeAll
    public static void initializeHandler() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("config.json"), StandardCharsets.ISO_8859_1)) {
            json = new Gson().fromJson(br, JsonObject.class);
        } catch (IOException e) {
            LOGGER.error("Config can't be found, ", e);
        }
        Map<String, List<Amazon>> reviewAsinMap = new ReadFile(json.get("find").getAsJsonObject().get("review").getAsString()).readFile(AmazonReview.class);
        search = new ReviewSearchHandler(reviewAsinMap);
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
