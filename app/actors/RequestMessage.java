package actors;

import play.mvc.Http;
import play.mvc.Results;

/**
 * Created by mszczap on 27.02.14.
 */
public class RequestMessage {

    private Http.Request request;
    private Http.Response response;
    private Results.Chunks.Out<String> out;

    public RequestMessage(Http.Request request, Http.Response response, Results.Chunks.Out<String> out) {
        this.request = request;
        this.response = response;
        this.out = out;
    }

    public Results.Chunks.Out<String> getOut() {
        return out;
    }

    public Http.Request getRequest() {
        return request;
    }

    public Http.Response getResponse() {
        return response;
    }

}
