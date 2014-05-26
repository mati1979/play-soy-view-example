package hysterix;

import controllers.WSUtils;
import play.libs.ws.WSRequestHolder;

import java.util.Optional;

/**
 * Created by mati on 26/05/2014.
 */
public abstract class WSCommand<T> extends HysterixCommand<T> {

    protected WSCommand(HystrixRequestCache hystrixRequestCache) {
        super(hystrixRequestCache);
    }

    @Override
    public Optional<String> getCacheKey() {
        return Optional.of(String.valueOf(WSUtils.hashCode(getHolder())));
    }

    protected abstract WSRequestHolder getHolder();

}
