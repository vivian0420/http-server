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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * FindHandler class. Handle requests and make responses
 */
public class FindHandler implements Handler {

    private static final Logger LOGGER = LogManager.getLogger(FindHandler.class.getName());

    private final Map<String, List<Amazon>> reviewAsinMap;
    private final Map<String, List<Amazon>> qaAsinMap;
    private JsonObject json;

    /**
     * Constructor. Read files and build asin hashmaps.
     */
    public FindHandler() {


        try (BufferedReader br = Files.newBufferedReader(Paths.get("config.json"), StandardCharsets.ISO_8859_1)) {
            json = new Gson().fromJson(br, JsonObject.class);
        } catch (IOException e) {
            LOGGER.error("Config can't be found, ", e);
        }
        reviewAsinMap = new ReadFile(json.get("find").getAsJsonObject().get("review").getAsString()).readFile(AmazonReview.class);
        qaAsinMap = new ReadFile(json.get("find").getAsJsonObject().get("qa").getAsString()).readFile(AmazonQA.class);
    }


    /**
     * Respond based on the requests.
     *
     * @param request  client requests
     * @param response an instance of ServerResponse
     */
    @Override
    public void handle(ServerRequest request, ServerResponse response) {

        //If the request method is "GET", return a web page containing(a text box and a button)
        if (request.getRequestMethod().equals("GET")) {
            String content = GetApplicationHTML.getApplicationHTML("find", "/find", "asin", "");
            response.response(content);
        }

        //If the request method is "POST", respond based on the content
        else if (request.getRequestMethod().equals("POST")) {
            StringBuilder result = new StringBuilder();
            if (request.getContent() == null) {
                response.setCode(400);
                response.response("<html>400 Bad Request</html>");
                return;
            }
            String[] contentParts = request.getContent().split("=");

            if(!contentParts[0].equals("asin")) {
                response.setCode(400);
                response.response("<html>400 Bad Request</html>");
                return;
            }
            else if (contentParts.length == 1) {
                String content = GetApplicationHTML.getApplicationHTML("find", "/find", "asin", "Please enter");
                response.response(content);
            } else if (contentParts.length == 2) {

                String asin = contentParts[1];
                if (!reviewAsinMap.containsKey(asin) && !qaAsinMap.containsKey(asin)) {
                    String content = GetApplicationHTML.getApplicationHTML("find", "/find", "asin", "0 result showed");
                    response.response(content);
                } else {
                    List<Amazon> reviewList = reviewAsinMap.get(asin);
                    List<Amazon> qaList = qaAsinMap.get(asin);

                    if (reviewList != null && qaList == null) {
                        result.append("<p>").append(reviewList.size()).append(" result(s) showed").append("</p>");
                        for (Amazon amazon : reviewList) {
                            result.append("<p>").append(amazon).append("</p>");
                        }
                        String content = GetApplicationHTML.getApplicationHTML("find", "/find", "asin", result.toString());
                        response.response(content);
                    } else if (reviewList == null && qaList != null) {
                        result.append("<p>").append(qaList.size()).append(" result(s) showed").append("</p>");
                        for (Amazon amazon : qaList) {
                            result.append("<p>").append(amazon).append("</p>");
                        }
                        String content = GetApplicationHTML.getApplicationHTML("find", "/find", "asin", result.toString());
                        response.response(content);
                    } else {
                        result.append("<p>").append(reviewList.size() + qaList.size()).append(" result(s) showed").append("</p>");
                        for (Amazon amazon : reviewList) {
                            result.append("<p>").append(amazon).append("</p>");
                        }
                        for (Amazon amazon : qaList) {
                            result.append("<p>").append(amazon).append("</p>");
                        }
                        String content = GetApplicationHTML.getApplicationHTML("find", "/find", "asin", result.toString());
                        response.response(content);

                    }
                }
            }
        }
    }
}
