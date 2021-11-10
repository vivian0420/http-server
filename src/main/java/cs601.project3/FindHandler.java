package cs601.project3;

import org.apache.commons.text.StringEscapeUtils;

import java.util.List;
import java.util.Map;

/**
 * FindHandler class. Handle requests and make responses
 */
public class FindHandler implements Handler {


    private final Map<String, List<Amazon>> reviewAsinMap;
    private final Map<String, List<Amazon>> qaAsinMap;

    /**
     * Constructor. Read files and build asin hashmaps.
     */
    public FindHandler(Map<String, List<Amazon>> reviewAsinMap, Map<String, List<Amazon>> qaAsinMap) {
        this.reviewAsinMap = reviewAsinMap;
        this.qaAsinMap = qaAsinMap;
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
                response.response("<html xmlns=\"http://www.w3.org/1999/xhtml\">400 Bad Request</html>");
                return;
            }
            String[] contentParts = request.getContent().split("=");

            if (!contentParts[0].equals("asin")) {
                response.setCode(400);
                response.response("<html xmlns=\"http://www.w3.org/1999/xhtml\">400 Bad Request</html>");
            } else if (contentParts.length == 1) {
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
                            result.append("<p>").append(StringEscapeUtils.escapeHtml4(amazon.toString())).append("</p>\n");
                        }
                        String content = GetApplicationHTML.getApplicationHTML("find", "/find", "asin", result.toString());
                        response.response(content);
                    } else if (reviewList == null && qaList != null) {
                        result.append("<p>").append(qaList.size()).append(" result(s) showed").append("</p>");
                        for (Amazon amazon : qaList) {
                            result.append("<p>").append(StringEscapeUtils.escapeHtml4(amazon.toString())).append("</p>\n");
                        }
                        String content = GetApplicationHTML.getApplicationHTML("find", "/find", "asin", result.toString());
                        response.response(content);
                    } else {
                        result.append("<p>").append(reviewList.size() + qaList.size()).append(" result(s) showed").append("</p>");
                        for (Amazon amazon : reviewList) {
                            result.append("<p>").append(StringEscapeUtils.escapeHtml4(amazon.toString())).append("</p>\n");
                        }
                        for (Amazon amazon : qaList) {
                            result.append("<p>").append(StringEscapeUtils.escapeHtml4(amazon.toString())).append("</p>\n");
                        }
                        String content = GetApplicationHTML.getApplicationHTML("find", "/find", "asin", result.toString());
                        response.response(content);
                    }
                }
            }
        }
    }
}
