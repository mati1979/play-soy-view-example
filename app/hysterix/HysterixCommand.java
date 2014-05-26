package hysterix;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import play.libs.F;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by mati on 26/05/2014.
 */
public abstract class HysterixCommand<T> {

    protected final AtomicBoolean isExecutionComplete = new AtomicBoolean(false);

    protected List<HysterixEventType> executionEvents = Collections.synchronizedList(Lists.newArrayList());

    private AtomicReference<Stopwatch> stopwatch = new AtomicReference(Stopwatch.createUnstarted());

    protected HysterixSettings hysterixSettings = new HysterixSettings();

    //transform response into domain object, client is responsible to call web service
    //this method is used only if needed, execute will work out if the response can come from a cache
    protected abstract F.Promise<T> run();

    private HystrixRequestCache hystrixRequestCache;

    protected HysterixCommand(final HystrixRequestCache hystrixRequestCache) {
        this.hystrixRequestCache = hystrixRequestCache;
    }

    public abstract String getCommandKey();

    public Optional<String> getCommandGroupKey() {
        return Optional.empty();
    }

    public Optional<String> getCacheKey() {
        return Optional.empty();
    }

    public F.Promise<T> execute() {
        stopwatch.get().start();
        return work()
                .transform(response -> onSuccess(response), t -> onFailure(t))
                .recover(t -> onRecover(t));
    }

    //checks cache or does invoke run method
    private F.Promise<T> work() {
        return getFromCache().orElse(run());
    }

    private T onSuccess(final T response) {
        stopwatch.get().stop();
        executionEvents.add(HysterixEventType.SUCCESS);
        isExecutionComplete.set(true);

        putToCache(response);

        return response;
    }

    private Optional<F.Promise<T>> getFromCache() {
        if (hysterixSettings.isRequestCacheEnabled() && getCacheKey().isPresent()) {
            final Optional<Object> possibleValue = hystrixRequestCache.get(getCacheKey().get());
            if (possibleValue.isPresent()) {
                final T value = (T) possibleValue.get();
                executionEvents.add(HysterixEventType.RESPONSE_FROM_CACHE);
                return Optional.of(F.Promise.pure(value));
            }
        }

        return Optional.empty();
    }

    private boolean putToCache(final T t) {
        if (hysterixSettings.isRequestCacheEnabled() && getCacheKey().isPresent()) {
            hystrixRequestCache.put(getCacheKey().get(), t);
            return true;
        }

        return false;
    }

    private Throwable onFailure(final Throwable t) {
        stopwatch.get().stop();
        System.out.println("onFailure:" + t);
        executionEvents.add(HysterixEventType.FAILURE);
        isExecutionComplete.set(true);
        return t;
    }

    private T onRecover(final Throwable t) throws Throwable {
        System.out.println("onRecover:" + t);

        if (hysterixSettings.isFallbackEnabled()) {
            System.out.println("onRecover - fallback enabled");
            return getFallback().map(response -> onRecoverSuccess(response))
                    .orElseThrow(() -> onRecoverFailure(t));
        }

        throw t;
    }

    private T onRecoverSuccess(final T t) {
        isExecutionComplete.set(true);
        executionEvents.add(HysterixEventType.FALLBACK_SUCCESS);
        return t;
    }

    private Throwable onRecoverFailure(final Throwable t) {
        if (t instanceof TimeoutException) {
            executionEvents.add(HysterixEventType.TIMEOUT);
        }
        executionEvents.add(HysterixEventType.FAILURE);
        executionEvents.add(HysterixEventType.FALLBACK_FAILURE);

        return t;
    }

    public Optional<T> getFallback() {
        return Optional.empty();
    }

    public boolean isExecutionComplete() {
        return isExecutionComplete.get();
    }

    public boolean isSuccessfulExecution() {
        return executionEvents.contains(HysterixEventType.SUCCESS);
    }

    public boolean isFailedExecution() {
        return executionEvents.contains(HysterixEventType.FAILURE);
    }

    public boolean isResponseFromFallback() {
        return executionEvents.contains(HysterixEventType.FALLBACK_SUCCESS);
    }

    public boolean isResponseTimeout() {
        return executionEvents.contains(HysterixEventType.TIMEOUT);
    }

    public boolean isResponseFromCache() {
        return executionEvents.contains(HysterixEventType.RESPONSE_FROM_CACHE);
    }

    public long getExecutionTime(final TimeUnit timeUnit) {
        return stopwatch.get().elapsed(timeUnit);
    }

    public List<HysterixEventType> getExecutionEvents() {
        return Lists.newArrayList(executionEvents);
    }

}
