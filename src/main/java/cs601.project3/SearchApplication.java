package cs601.project3;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchApplication {

    private static final Logger LOGGER = LogManager.getLogger(FindHandler.class.getName());

    public static void main(String[] args) {
        int port = 8080;
        HTTPServer server = new HTTPServer(port);
        //The request GET /reviewsearch will be dispatched to the
        //handle method of the ReviewSearchHandler.
        server.addMapping("/reviewsearch", new ReviewSearchHandler(getReviewAsin()));
        //The request GET /find will be dispatched to the
        //handle method of the FindHandler.
        server.addMapping("/find", new FindHandler(getReviewAsin(), getQaAsin()));
        server.startup();
    }

    private static Map<String, List<Amazon>> getReviewAsin() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("config.json"), StandardCharsets.ISO_8859_1)) {
            return new ReadFile(new Gson().fromJson(br, JsonObject.class).get("find").getAsJsonObject().get("review").getAsString())
                    .readFile(AmazonReview.class);
        } catch (IOException e) {
            LOGGER.error("Config can't be found, ", e);
            return new HashMap<>();
        }
    }

    private static Map<String, List<Amazon>> getQaAsin() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("config.json"), StandardCharsets.ISO_8859_1)) {
            return new ReadFile(new Gson().fromJson(br, JsonObject.class).get("find").getAsJsonObject().get("qa").getAsString())
                    .readFile(AmazonQA.class);
        } catch (IOException e) {
            LOGGER.error("Config can't be found, ", e);
            return new HashMap<>();
        }
    }
}
