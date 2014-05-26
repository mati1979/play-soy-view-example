package controllers;

import play.libs.F;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

import java.util.UUID;

/**
 * Created by mati on 25/05/2014.
 */
public class HttpRequest {

    private String requestId;

    private HttpRequestsCache httpRequestsCache;

    public HttpRequest(final HttpRequestsCache httpRequestsCache, final String prefix, final WSRequestHolder holder) {
        this.requestId = prefix.concat(":").concat(UUID.randomUUID().toString());
        this.httpRequestsCache = httpRequestsCache.addRequest(requestId, holder);
    }

    public HttpRequest(final HttpRequestsCache httpRequestsCache, final WSRequestHolder holder) {
        this.requestId = UUID.randomUUID().toString();
        this.httpRequestsCache = httpRequestsCache.addRequest(requestId, holder);
    }

    public String getRequestId() {
        return requestId;
    }

    public F.Promise<WSResponse> get() {
        return httpRequestsCache.get(requestId);
    }

}
