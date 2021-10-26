package cs601.project3;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class ReviewSearchHandler implements Handler {

    private final InvertedIndex<Amazon> index;


    /**
     *
     */
    public ReviewSearchHandler() {

        Map<String, List<Amazon>> asinMap = new ReadFile("reviews_Cell_Phones_and_Accessories_5.json").readFile(AmazonReview.class);
        this.index = new InvertedIndex<>(asinMap);
    }

    /**
     *
     * @param request
     * @param response
     */
    @Override
    public void handle(ServerRequest request, ServerResponse response) {

        if (request.getRequestMethod().equals("GET")) {
            String content = GetApplicationHTML.getApplicationHTML("reviewsearch", "/reviewsearch",
                                                                         "query", "");
            response.response(content);

        } else if (request.getRequestMethod().equals("POST")) {

            String[] contentParts = request.getContent().split("=");
            StringBuilder result = new StringBuilder();
            if (contentParts.length == 1) {
                String content = GetApplicationHTML.getApplicationHTML("reviewsearch", "/reviewsearch",
                        "query", "Please enter");
                response.response(content);

            } else if (contentParts.length == 2) {
                String term = contentParts[1];
                List<Amazon> list = index.search(term);
                if (list != null) {

                    result.append("<p>").append(list.size()).append(" result(s) showed").append("</p>");
                    for (Amazon review : list) {
                        result.append("<p>").append(review).append("</p>");
                    }
                    String content = GetApplicationHTML.getApplicationHTML("reviewsearch", "/reviewsearch",
                            "query", result.toString());
                    response.response(content);

                } else {
                    result.append("<p>").append(term).append(" is not found").append("</p");
                    String content = GetApplicationHTML.getApplicationHTML("reviewsearch", "/reviewsearch",
                            "query", result.toString());
                    response.response(content);
                }
            }
        }
    }
}
