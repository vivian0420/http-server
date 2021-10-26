package cs601.project3;

import java.io.PrintWriter;

/**
 *
 */
public class ServerResponse {
    private int code;
    private PrintWriter output;
    //private Map<String, String> headers;

    public ServerResponse(PrintWriter output) {
        this.code = 200;
        this.output = output;

    }

    /**
     *
     * @param code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     *
     * @param content
     */
    public void response(String content) {
        this.output.println("HTTP/1.1 " + this.code);
        this.output.println("content-length: " + content.length());
        this.output.println("");
        this.output.println(content);
    }
}
