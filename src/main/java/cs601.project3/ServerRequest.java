package cs601.project3;

import java.util.Arrays;
import java.util.Map;

/**
 * Parse request line
 */
public class ServerRequest {

    final private String requestLine;
    final private Map<String, String> headers;
    final private String content;
    final private String path;
    final private String requestMethod;
    final private String version;

    /**
     * Constructor. Parse request line
     * @param requestLine HTTP request line, for example: GET /find HTTP/1.1
     * @param headers HTTP headers
     * @param content HTTP post content
     */
    public ServerRequest(String requestLine, Map<String, String> headers, String content) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.content = content;

        if (requestLine != null) {
            String[] requestLineParts = requestLine.split(" ");
            if (requestLineParts.length == 3) {
                this.requestMethod = requestLineParts[0];
                this.path = requestLineParts[1];
                this.version = requestLineParts[2];
            } else {
                this.requestMethod = null;
                this.path = null;
                this.version = null;
            }
        } else {
            this.requestMethod = null;
            this.path = null;
            this.version = null;
        }
    }


    /**
     * check if a request is bad request or not.
     * @return return true if the request line is bad request,and vice versa
     */
    public boolean is400() {
        String[] validMethods = {"GET", "POST", "PUT", "HEAD", "OPTIONS", "DELETE", "CONNECT", "PATCH", "TRACE"};
        return this.requestMethod == null || this.path == null || this.version == null
                || !path.startsWith("/") || !Arrays.asList(validMethods).contains(requestMethod);
    }

    /**
     * check if a request method is allowed or not.
     * @return return true if the request method is not allowed,and vice versa
     */
    public boolean is405() {
        return !this.requestMethod.equals("GET") && !this.requestMethod.equals("POST") ;
    }

    /**
     * getter method
     * @return post content
     */
    public String getContent() {
        return this.content;
    }

    /**
     * getter method
     * @return request path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * getter method
     * @return request method
     */
    public String getRequestMethod() {
        return this.requestMethod;
    }


}
