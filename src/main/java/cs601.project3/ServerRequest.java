package cs601.project3;

public class ServerRequest {
    private String requestMethod;
    private String requestPath;
    private String header;
    private String content;

    public ServerRequest(String requestMethod, String requestPath, String header, String content) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.header = header;
        this.content = content;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
