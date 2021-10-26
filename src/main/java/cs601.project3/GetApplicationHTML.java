package cs601.project3;

/**
 * Utility class for displaying HTML
 */
public class GetApplicationHTML {

    /**
     *
     * @param applicationName  name of application,like find, reviewsearch
     * @param actionPath HTTP request path
     * @param name name of input, like asin, query, message
     * @param results the result from either "find" of "reviewsearch" or "Chat"
     * @return HTML string
     */
    public static String getApplicationHTML(String applicationName, String actionPath, String name, String results) {
        return String.format("""
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
                       <html xmlns="http://www.w3.org/1999/xhtml">
                         <head>
                           <title>%s Application</title>
                         </head>
                         <body>
                          <form action="%s" method="post" accept-charset="utf-8">
                            <input type="text" name="%s" value=""/>
                            <input type="submit" value="%s" />
                          </form>
                          %s
                         </body>
                       </html>
                       """, applicationName, actionPath, name, applicationName, results );

    }
}
