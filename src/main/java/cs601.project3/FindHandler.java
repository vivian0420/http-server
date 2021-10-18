package cs601.project3;

import java.util.List;
import java.util.Map;

public class FindHandler implements Handler {
    private Map<String, List<Amazon>> reviewAsinMap;
    private Map<String, List<Amazon>> qaAsinMap;

    public FindHandler() {

        reviewAsinMap = new ReadFile("reviews_Cell_Phones_and_Accessories_5.json").readFile(AmazonReview.class);
        qaAsinMap = new ReadFile("qa_Cell_Phones_and_Accessories.json").readFile(AmazonQA.class);
    }


    @Override
    public void handle(ServerRequest request, ServerResponse response) {

        String page = """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
                           <html xmlns="http://www.w3.org/1999/xhtml">
                             <head>
                               <title>Search Application</title>
                             </head>
                             <body>
                              <form action="/find" method="post">
                                <input type="text" id="search" name="asin" value="">
                                <input type="submit" value="Search">
                              </form>
                             </body>
                           </html>
                """;
        if (request.getRequestMethod().equals("GET")) {

            response.response(page);
        } else if (request.getRequestMethod().equals("POST")) {

            String asin = request.getContent().split("=")[1];
            StringBuilder result = new StringBuilder();
            result.append(page);
            if (!reviewAsinMap.containsKey(asin) && !qaAsinMap.containsKey(asin)) {
                result.append("<p>").append(asin).append(" is not found").append("</p>");
                response.response(result.toString());
            } else {
                if (reviewAsinMap.containsKey(asin)) {

                    List<Amazon> list = reviewAsinMap.get(asin);
                    for (Amazon amazon : list) {
                        result.append("<p>").append(amazon).append("</p>");
                    }
                    response.response(result.toString());
                }
                if (qaAsinMap.containsKey(asin)) {
                    List<Amazon> list = qaAsinMap.get(asin);
                    for (Amazon amazon : list) {
                        result.append("<p>").append(amazon).append("</p>");
                    }
                }
                response.response(result.toString());
            }
        }
    }
}
