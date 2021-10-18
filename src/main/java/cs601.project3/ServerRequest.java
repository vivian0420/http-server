package cs601.project3;

import java.util.Map;

public class ServerRequest {
    private String requestLine;
    private Map<String, String> headers;
    private String content;
    private String path;
    private String requestMethod;
    private String version;

    public ServerRequest(String requestLine, Map<String, String> headers, String content) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.content = content;

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

    public boolean is400() {
        return this.requestMethod == null || this.path == null || this.version == null;
    }

    public boolean is405() {
        return !this.requestMethod.equals("GET") && !this.requestMethod.equals("POST");
    }

    public String getContent() {
        return this.content;
    }

    public String getPath() {
        return this.path;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }
}
