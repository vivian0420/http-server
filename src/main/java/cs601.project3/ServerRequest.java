package cs601.project3;

import java.util.Arrays;
import java.util.Map;

/**
 *
 */
public class ServerRequest {
    private String requestLine;
    private Map<String, String> headers;
    private String content;
    private String path;
    private String requestMethod;
    private String version;

    /**
     *
     * @param requestLine
     * @param headers
     * @param content
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
        }
    }


    /**
     *
     * @return
     */
    public boolean is400() {
        String[] validMethods = {"GET", "POST", "PUT", "HEAD", "OPTIONS", "DELETE", "CONNECT", "PATCH", "TRACE"};
        return this.requestMethod == null || this.path == null || this.version == null
                || !path.startsWith("/") || !Arrays.asList(validMethods).contains(requestMethod);
    }

    /**
     *
     * @return
     */
    public boolean is405() {
        return !this.requestMethod.equals("GET") && !this.requestMethod.equals("POST") ;
    }

    /**
     *
     * @return
     */
    public String getContent() {
        return this.content;
    }

    /**
     *
     * @return
     */
    public String getPath() {
        return this.path;
    }

    /**
     *
     * @return
     */
    public String getRequestMethod() {
        return this.requestMethod;
    }
}
