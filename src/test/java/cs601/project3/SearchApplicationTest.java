package cs601.project3;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchApplicationTest {
    private static HTTPServer server;
    private final static int PORT = 8420;
    private static final Logger LOGGER = LogManager.getLogger(FindHandler.class.getName());
    private static JsonObject json;
    @BeforeAll
    public static void startApplication() {
        server = new HTTPServer(PORT);
        try (BufferedReader br = Files.newBufferedReader(Paths.get("config.json"), StandardCharsets.ISO_8859_1)) {
            json = new Gson().fromJson(br, JsonObject.class);
        } catch (IOException e) {
            LOGGER.error("Config can't be found, ", e);
        }
        Map<String, List<Amazon>> reviewAsinMap = new ReadFile(json.get("find").getAsJsonObject().get("review").getAsString()).readFile(AmazonReview.class);
        Map<String, List<Amazon>>qaAsinMap = new ReadFile(json.get("find").getAsJsonObject().get("qa").getAsString()).readFile(AmazonQA.class);
        server.addMapping("/reviewsearch", new ReviewSearchHandler(reviewAsinMap));
        server.addMapping("/find", new FindHandler(reviewAsinMap, qaAsinMap));
        server.startup();
    }

    @Test
    public void testInvalidPathReviewSearch() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/reviewsearching")).GET()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r->{
                    assertEquals(r.statusCode(),404);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("404 Not Found"));


                }).join();
    }

    @Test
    public void testInvalidPathFind() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/finded")).GET()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r->{
                    assertEquals(r.statusCode(),404);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("404 Not Found"));


                }).join();
    }

    @Test
    public void testInvalidMethodFind() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/find")).DELETE()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r->{
                    assertEquals(r.statusCode(),405);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("405 Method Not Allowed"));


                }).join();
    }

    @Test
    public void testInvalidMethodSearch() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/reviewsearch")).DELETE()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r->{
                    assertEquals(r.statusCode(),405);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("405 Method Not Allowed"));


                }).join();
    }

    @Test
    public void testValidReviewSearchGET() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/reviewsearch")).GET()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r->{
                    assertEquals(r.statusCode(),200);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("!DOCTYPE html PUBLIC"));
                    assertTrue(r.body().contains("<title>reviewsearch Application</title>"));
                }).join();
    }

    @Test
    public void testValidReviewSearchPOST() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/reviewsearch")).POST(HttpRequest.BodyPublishers.ofString("query=appl"))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r -> {
                    assertEquals(r.statusCode(), 200);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("!DOCTYPE html PUBLIC"));
                    assertTrue(r.body().contains("<title>reviewsearch Application</title>"));
                    assertTrue(r.body().contains("is not found"));
                }).join();
    }

    @Test
    public void testValidFindPOST() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/find")).POST(HttpRequest.BodyPublishers.ofString("asin=3998899561"))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r->{
                    assertEquals(r.statusCode(),200);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("!DOCTYPE html PUBLIC"));
                    assertTrue(r.body().contains("<title>find Application</title>"));
                    assertTrue(r.body().contains("result(s) showed"));
                }).join();
    }

    @Test
    public void testValidFindGET() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/find")).GET()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r->{
                    assertEquals(r.statusCode(),200);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("!DOCTYPE html PUBLIC"));
                    assertTrue(r.body().contains("<title>find Application</title>"));

                }).join();

    }
}
