package cs601.project3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchApplicationTest {
    private static HTTPServer server;
    private final static int PORT = 8420;

    @BeforeAll
    public static void startApplication() {
        server = new HTTPServer(PORT);
        server.addMapping("/reviewsearch", new ReviewSearchHandler());
        server.addMapping("/find", new FindHandler());
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
