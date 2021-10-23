package cs601.project3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ChatHandler implements Handler{
    @Override
    public void handle(ServerRequest request, ServerResponse response) {

        if (request.getRequestMethod().equals("GET")) {
            String content = new GetApplicationHTML().GetApplicationHTML("Chat", "/slackbot",
                    "message", "");
            response.response(content);

        } else if (request.getRequestMethod().equals("POST")) {

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
                        .thenAccept(r -> response.response(new GetApplicationHTML().GetApplicationHTML("Chat", "/slackbot",
                                "message","<pre>" + new GsonBuilder().setPrettyPrinting()
                                .create().toJson(new Gson().fromJson(r, JsonObject.class) + "</pre>"))))
                        .join();
            }
        }
    }

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
