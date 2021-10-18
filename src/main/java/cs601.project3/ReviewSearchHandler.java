package cs601.project3;

import java.util.List;
import java.util.Map;

public class ReviewSearchHandler implements Handler {

    private InvertedIndex<AmazonReview> index;


    public ReviewSearchHandler() {

        Map<String, List<Amazon>> asinMap = new ReadFile("reviews_Cell_Phones_and_Accessories_5.json").readFile(AmazonReview.class);
        this.index = new InvertedIndex(asinMap);
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
                          <form action="/reviewsearch" method="post">
                            <input type="text" id="search" name="query" value="">
                            <input type="submit" value="Search">
                          </form>
                         </body>
                       </html>
                       
                """;
        if (request.getRequestMethod().equals("GET")) {

            response.response(page);
        } else if (request.getRequestMethod().equals("POST")) {
            String term = request.getContent().split("=")[1];
            List<AmazonReview> list = index.search(term);
            StringBuilder result = new StringBuilder();
            if (list != null) {
                result.append(page);
                for (AmazonReview review : list) {
                    result.append("<p>").append(review).append("</p>");
                }
                response.response(result.toString());
            } else {
                result.append("<p>").append(page).append(term).append(" is not found").append("</p");
                response.response(result.toString());
            }

        }


    }
}
