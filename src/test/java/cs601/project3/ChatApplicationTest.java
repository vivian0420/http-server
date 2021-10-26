package cs601.project3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatApplicationTest {

    private static HTTPServer server;
    private final static int PORT = 8421;

    @BeforeAll
    public static void startApplication() {
        server = new HTTPServer(PORT);
        server.addMapping("/slackbot", new ChatHandler());
        server.startup();
    }

    @Test
    public void testValidGET(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/slackbot")).GET()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r->{
                    assertEquals(r.statusCode(),200);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("!DOCTYPE html PUBLIC"));
                    assertTrue(r.body().contains("<title>Chat Application</title>"));
                }).join();

    }

    @Test
    public void testInvalidGET(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/slackbottle")).GET()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r->{
                    assertEquals(r.statusCode(),404);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("404 Not Found"));

                }).join();
    }

    @Test
    public void testValidPOST(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/slackbot")).POST(HttpRequest.BodyPublishers.ofString("message=Hello World"))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r->{
                    assertEquals(r.statusCode(),200);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("!DOCTYPE html PUBLIC"));
                    assertTrue(r.body().contains("<title>Chat Application</title>"));
                    assertTrue(r.body().contains("true"));

                }).join();

    }

    @Test
    public void testInvalidPOST(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/slackbottle")).POST(HttpRequest.BodyPublishers.ofString("message=Hello World"))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r->{
                    assertEquals(r.statusCode(),404);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("404 Not Found"));
                }).join();

    }

    @Test
    public void testValidEmptyPOST() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/slackbot")).POST(HttpRequest.BodyPublishers.ofString("message="))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(r -> {
                    assertEquals(r.statusCode(), 200);
                    assertTrue(r.headers().map().containsKey("content-length"));
                    assertTrue(r.body().contains("!DOCTYPE html PUBLIC"));
                    assertTrue(r.body().contains("<title>Chat Application</title>"));
                    assertTrue(r.body().contains("false"));

                }).join();
    }

        @Test
        public void testInvalidMethod(){
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + PORT + "/slackbot")).DELETE()
                    .build();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(r->{
                        assertEquals(r.statusCode(),405);
                        assertTrue(r.headers().map().containsKey("content-length"));
                        assertFalse(r.body().contains("!DOCTYPE html PUBLIC"));
                        assertFalse(r.body().contains("<title>Chat Application</title>"));
                        assertTrue(r.body().contains("405 Method Not Allowed"));

                    }).join();
    }

}
