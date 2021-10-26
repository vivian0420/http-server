package cs601.project3;

import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * ChatHandler class. Handle requests and make responses
 */
public class ChatHandler implements Handler{

    /**
     * Respond based on the requests.
     * @param request the request that the server received
     * @param response instance of ServerResponse
     */
    @Override
    public void handle(ServerRequest request, ServerResponse response) {

        //If the request method is "GET", return a web page containing(a text box and a button)
        if (request.getRequestMethod().equals("GET")) {
            String content = GetApplicationHTML.getApplicationHTML("Chat", "/slackbot",
                    "message", "");
            response.response(content);
        }

        //If the request method is "POST", prepare a JsonObject for posting to Slack. Then create a client, send the
        // JsonObject to Slack and handle the message the Slack sends back.
        else if (request.getRequestMethod().equals("POST")) {

            String postTerm = getTerm(request.getContent());
            if (postTerm != null) {

                JsonObject object = new JsonObject();
                object.addProperty("channel", "C02HW1RJ5JR");
                object.addProperty("text", postTerm);


                HttpClient client = HttpClient.newHttpClient();
                HttpRequest slackRequest = HttpRequest.newBuilder().uri(URI.create("https://slack.com/api/chat.postMessage"))
                        .header("Authorization", "Bearer xoxb-2611054740403-2616894378805-xvfMuItdSeRlyGqCI3q8ARaz")
                        .header("Content-Type", "application/json; utf-8")
                        .POST(HttpRequest.BodyPublishers.ofString(object.toString()))
                        .build();
                client.sendAsync(slackRequest, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenAccept(r -> response.response(GetApplicationHTML.getApplicationHTML("Chat", "/slackbot",
                                "message",r)))
                        .join();
            }
         }
    }

    /**
     * Get the term that users want to post
     * @param content The content posted by the browser.
     * @return the term that users want to post
     */
    public String getTerm(String content) {
        try {
            String[] split = URLDecoder.decode(content, StandardCharsets.UTF_8.name()).split("=");
            if(split.length > 1) {
                return split[1];
            }
            else {
                return "";
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
