package hysterix;

import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;

import java.util.Optional;

/**
 * Created by mati on 26/05/2014.
 */
public class HelloFallbackCommand extends WSCommand<String> {

    private WSRequestHolder wsRequestHolder = WS.url("http://localhost:9000/mock").setTimeout(10000);

    public HelloFallbackCommand(final HystrixRequestCache httpRequestsCache) {
        super(httpRequestsCache);
    }

    @Override
    protected F.Promise<String> run() {
        return wsRequestHolder.get().map(response -> response.getBody());
    }

    @Override
    public String getCommandKey() {
        return "hello.command.fallback";
    }

    @Override
    protected WSRequestHolder getHolder() {
        return wsRequestHolder;
    }

    @Override
    public Optional<String> getFallback() {
        return Optional.of("fallback");
    }

}
