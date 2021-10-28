package cs601.project3;

import java.io.PrintWriter;

/**
 * ServerResponse class. set up the content that we want to respond to user based on user request
 */
public class ServerResponse {
    private int code;
    private PrintWriter output;

    public ServerResponse(PrintWriter output) {
        this.code = 200;
        this.output = output;

    }

    /**
     * Set response code
     * @param code The response code that we want to respond to user
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Response for user request
     * @param content the content that we want to respond to user based on user request
     */
    public void response(String content) {
        this.output.println("HTTP/1.1 " + this.code);
        this.output.println("content-length: " + content.length());
        this.output.println("");
        this.output.println(content);
    }
}
