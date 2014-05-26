package controllers;

import org.springframework.stereotype.Component;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by mati on 26/05/2014.
 */
@Component
public class RequestCacheController extends Controller {

    public F.Promise<Result> index() {
        final HttpRequestsCache httpRequestsCache = new HttpRequestsCache("play-soy-view-example");

        final F.Promise<WSResponse> responseP1 = httpRequestsCache.createHttpRequest("POD1", WS.url("http://localhost:9000/index2")).get();
        final F.Promise<WSResponse> responseP2 = httpRequestsCache.createHttpRequest("POD2", WS.url("http://localhost:9000/index2")).get();
        final F.Promise<WSResponse> responseP3 = httpRequestsCache.createHttpRequest("POD3", WS.url("http://localhost:9000/index2").setQueryParameter("param1", "param2")).get();
        final F.Promise<WSResponse> responseP4 = httpRequestsCache.createHttpRequest("POD4", WS.url("http://localhost:9000/index2").setQueryParameter("param1", "param2")).get();

        responseP1.onRedeem(resp -> System.out.println("resp1_SUCCESS:" + resp.getBody()));
        responseP1.onFailure(t -> System.out.println("resp1_FAIL:" + t.getMessage()));

        responseP2.onRedeem(resp -> System.out.println("resp2_SUCCESS:" + resp.getBody()));
        responseP2.onFailure(t -> System.out.println("resp2_FAIL:" + t.getMessage()));

        responseP3.onRedeem(resp -> System.out.println("resp3_SUCCESS:" + resp.getBody()));
        responseP3.onFailure(t -> System.out.println("resp3_FAIL:" + t.getMessage()));

        responseP4.onRedeem(resp -> System.out.println("resp4_SUCCESS:" + resp.getBody()));
        responseP4.onFailure(t -> System.out.println("resp4_FAIL:" + t.getMessage()));

        return httpRequestsCache.getRequestsUrls().map(data -> ok(stats(data)));
    }

    private String stats(final Map<String, HttpRequestsCache.RemoteCallStats> stats) {
        System.out.println("stats:" + stats);
        final StringWriter writer = new StringWriter();
        for (final Map.Entry<String, HttpRequestsCache.RemoteCallStats> entry : stats.entrySet()) {
            final String requestId = entry.getKey();
            final HttpRequestsCache.RemoteCallStats remoteCallStats = entry.getValue();
            writer.write(String.format("url - requestId:%s,url:%s,time:%d ms, done:%s",
                    requestId, remoteCallStats.getUrl(), remoteCallStats.getTimeInMs(TimeUnit.MILLISECONDS),
                    remoteCallStats.isCompleted()));
            writer.write("\n");
        }

        return writer.toString();
    }

}
