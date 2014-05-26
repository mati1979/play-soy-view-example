package hysterix;

import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;

/**
 * Created by mati on 26/05/2014.
 */
public class HelloCommandNoFallback extends WSCommand<String> {

    private WSRequestHolder wsRequestHolder = WS.url("http://localhost:9000/mock").setTimeout(1000);

    public HelloCommandNoFallback(final HystrixRequestCache httpRequestsCache) {
        super(httpRequestsCache);
    }

    @Override
    protected F.Promise<String> run() {
        return wsRequestHolder.get().map(response -> response.getBody());
    }

    @Override
    protected WSRequestHolder getHolder() {
        return wsRequestHolder;
    }

    @Override
    public String getCommandKey() {
        return "hello.command.no.fallback";
    }

}
