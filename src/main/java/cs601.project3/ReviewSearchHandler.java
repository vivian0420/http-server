package cs601.project3;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * ChatHandler class. Handle requests and make responses
 */
public class ReviewSearchHandler implements Handler {
    private static final Logger LOGGER = LogManager.getLogger(ReviewSearchHandler.class.getName());

    private final InvertedIndex<Amazon> index;
    private JsonObject json;

    /**
     * Constructor. Read files and build asin hashmap.
     */
    public ReviewSearchHandler(Map<String, List<Amazon>> reviewAsinMap) {

        this.index = new InvertedIndex<>(reviewAsinMap);
    }

    /**
     * Respond based on the requests.
     *
     * @param request  the request that the server received
     * @param response instance of ServerResponse
     */
    @Override
    public void handle(ServerRequest request, ServerResponse response) {

        //If the request method is "GET", return a web page containing(a text box and a button)
        if (request.getRequestMethod().equals("GET")) {
            String content = GetApplicationHTML.getApplicationHTML("reviewsearch", "/reviewsearch",
                    "query", "");
            response.response(content);
        }

        //If the request method is "POST", respond based on the content
        else if (request.getRequestMethod().equals("POST")) {
            if (request.getContent() == null) {
                response.setCode(400);
                response.response("<html xmlns=\"http://www.w3.org/1999/xhtml\">400 Bad Request</html>");
                return;
            }
            String[] contentParts = request.getContent().split("=");
            StringBuilder result = new StringBuilder();
            //If search term is missing, prompt users to enter what they want to search.
            if(!contentParts[0].equals("query"))
            {
                response.setCode(400);
                response.response("<html xmlns=\"http://www.w3.org/1999/xhtml\">400 Bad Request</html>");
                return;
            }
            else if (contentParts.length == 1) {
                String content = GetApplicationHTML.getApplicationHTML("reviewsearch", "/reviewsearch",
                        "query", "Please enter");
                response.response(content);

            } else if (contentParts.length == 2) {
                String term = contentParts[1];
                List<Amazon> list = index.search(term);
                if (list != null) {

                    result.append("<p>").append(list.size()).append(" result(s) showed").append("</p>");
                    for (Amazon review : list) {
                        result.append("<p>").append(StringEscapeUtils.escapeHtml4(review.toString())).append("</p>");
                    }
                    String content = GetApplicationHTML.getApplicationHTML("reviewsearch", "/reviewsearch",
                            "query", result.toString());
                    response.response(content);

                } else {
                    result.append("<p>").append(term).append(" is not found").append("</p>");
                    String content = GetApplicationHTML.getApplicationHTML("reviewsearch", "/reviewsearch",
                            "query", result.toString());
                    response.response(content);
                }
            }
        }
    }
}
