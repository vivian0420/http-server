package cs601.project3;

public interface Handler {
    /**
     * Handler interface
     * @param request the request that the server received
     * @param response instance of ServerResponse
     */
    public void handle(ServerRequest request, ServerResponse response);

}
