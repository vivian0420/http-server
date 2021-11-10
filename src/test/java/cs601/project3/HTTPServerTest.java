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

public class HTTPServerTest {

    private static final Logger LOGGER = LogManager.getLogger(FindHandler.class.getName());
    private static JsonObject json;
    private static HTTPServer server;
    @BeforeAll
    public static void startTheServer() {
        server = new HTTPServer(8088);
        try (BufferedReader br = Files.newBufferedReader(Paths.get("config.json"), StandardCharsets.ISO_8859_1)) {
            json = new Gson().fromJson(br, JsonObject.class);
        } catch (IOException e) {
            LOGGER.error("Config can't be found, ", e);
        }
        Map<String, List<Amazon>> reviewAsinMap = new ReadFile(json.get("find").getAsJsonObject().get("review").getAsString()).readFile(AmazonReview.class);
        Map<String, List<Amazon>>qaAsinMap = new ReadFile(json.get("find").getAsJsonObject().get("qa").getAsString()).readFile(AmazonQA.class);
        server.addMapping("/find", new FindHandler(reviewAsinMap, qaAsinMap));
        server.addMapping("/reviewSearch", new ReviewSearchHandler(reviewAsinMap));
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
