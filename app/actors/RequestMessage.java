package actors;

import play.mvc.Http;

/**
 * Created by mszczap on 27.02.14.
 */
public class RequestMessage {

    private Http.Request request;
    private Http.Response response;

    public RequestMessage(Http.Request request, Http.Response response) {
        this.request = request;
        this.response = response;
    }

    public Http.Request getRequest() {
        return request;
    }

    public Http.Response getResponse() {
        return response;
    }

}
