package controllers;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import play.api.libs.ws.WSRequest;
import play.libs.F;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by mati on 24/05/2014.
 */
@NotThreadSafe
public class HttpRequestsCache {

    private String httpRequestId = UUID.randomUUID().toString();

    private Map<Integer, ClientsGroup> cache = Maps.newHashMap();

    private Map<String, Integer> requestIdsToGroupIds = Maps.newHashMap();

    public HttpRequestsCache(final String appPrefix) {
        this.httpRequestId = appPrefix.concat(":").concat(UUID.randomUUID().toString());
    }

    public String getHttpRequestId() {
        return httpRequestId;
    }

    public HttpRequestsCache addRequest(final String requestId, final WSRequestHolder holder) {
        final Integer clientsGroupId = WSUtils.hashCode(holder);
        requestIdsToGroupIds.put(requestId, clientsGroupId);

        final ClientsGroup clientsGroup = getOrCreateClientsGroup(clientsGroupId);

        clientsGroup.lazyProxyPromises.put(requestId, createLazyPromise());
        clientsGroup.wsRequestHolders.add(holder);

        cache.put(clientsGroupId, clientsGroup);

        return this;
    }

    public F.Promise<Map<String, RemoteCallStats>> getRequestsUrls() {
        return getRealPromises().map(promises -> requestsUrls());
    }

    private Map<String, RemoteCallStats> requestsUrls() {
        final Map<String, RemoteCallStats> urls = Maps.newHashMap();

        final Set<Map.Entry<String, Integer>> entries = requestIdsToGroupIds.entrySet();
        for (final Map.Entry<String, Integer> entry : entries) {
            final String requestId = entry.getKey();
            final Integer clientGroupId = entry.getValue();
            Optional.ofNullable(cache.get(clientGroupId)).ifPresent(clientsGroup -> {
                if (!clientsGroup.wsRequestHolders.isEmpty()) {
                    final WSRequestHolder next = clientsGroup.wsRequestHolders.iterator().next();
                    final String url = WSUtils.toUrl(next);
                    urls.put(requestId, new RemoteCallStats(url, clientsGroup.groupId, requestId, clientsGroup.stopwatch, clientsGroup.isCompleted));
                }
            });
        }

        return urls;
    }

    public HttpRequest createHttpRequest(final String prefix, final WSRequestHolder holder) {
        return new HttpRequest(this, prefix, holder);
    }

    public HttpRequest createHttpRequest(final WSRequestHolder holder) {
        return new HttpRequest(this, holder);
    }

    public F.Promise<WSResponse> get(final String requestId) {
        final Integer clientsGroupId = requestIdsToGroupIds.get(requestId);

        final ClientsGroup clientsGroup = getOrCreateClientsGroup(clientsGroupId);

        if (clientsGroup.realPromise.isPresent()) {
            return clientsGroup.getLazyPromise(requestId);
        }

        return realGet(requestId, clientsGroup);
    }

    private ClientsGroup getOrCreateClientsGroup(final Integer clientGroupId) {
        return cache.getOrDefault(clientGroupId, ClientsGroup.empty(clientGroupId));
    }

    private F.Promise<WSResponse> realGet(final String requestId, final ClientsGroup clientsGroup) {
        if (clientsGroup.wsRequestHolders.isEmpty()) {
            return F.Promise.throwing(new RuntimeException("You must first enqueue a holder via addRequest method!"));
        }

        final WSRequestHolder next = clientsGroup.wsRequestHolders.iterator().next();
        clientsGroup.stopwatch.start();
        final F.Promise<WSResponse> realResponseP = next.get();
        clientsGroup.realPromise = Optional.of(realResponseP);

        realResponseP.onRedeem(response -> clientsGroup.redeemSuccess(response));
        realResponseP.onFailure(t -> clientsGroup.redeemFailure(t));

        cache.put(clientsGroup.groupId, clientsGroup);

        return clientsGroup.getLazyPromise(requestId);
    }

    private scala.concurrent.Promise<WSResponse> createLazyPromise() {
        return scala.concurrent.Promise$.MODULE$.<play.libs.ws.WSResponse>apply();
    }

    private F.Promise<List<WSRequest>> getRealPromises() {
        final List<F.Promise<WSResponse>> collect = cache.values().stream().filter(clientsGroup -> clientsGroup.realPromise.isPresent())
                .map(clientsGroup -> clientsGroup.realPromise.get()).collect(Collectors.toList());

        final F.Promise<WSRequest>[] promises = new F.Promise[collect.size()];

        return F.Promise.sequence(collect.toArray(promises));
    }

    private static class ClientsGroup {

        private final Integer groupId;
        private Optional<F.Promise<WSResponse>> realPromise = Optional.empty();
        private Collection<WSRequestHolder> wsRequestHolders = Lists.newArrayList();
        private Map<String, scala.concurrent.Promise<WSResponse>> lazyProxyPromises = Maps.newHashMap();
        private boolean isCompleted = false;

        private Stopwatch stopwatch = Stopwatch.createUnstarted();

        private ClientsGroup(final Integer groupId) {
            this.groupId = groupId;
        }

        public F.Promise<WSResponse> getLazyPromise(final String requestId) {
            return F.Promise.<WSResponse>wrap(asScalaPromise(requestId).future());
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        private void redeemSuccess(final WSResponse response) {
            isCompleted = true;
            stopwatch.stop();
            lazyProxyPromises.values().stream().forEach(p -> p.success(response));
        }

        private void redeemFailure(final Throwable t) {
            isCompleted = true;
            stopwatch.stop();
            lazyProxyPromises.values().stream().forEach(p -> p.failure(t));
        }

        private scala.concurrent.Promise<WSResponse> asScalaPromise(final String requestId) {
            return lazyProxyPromises.get(requestId);
        }

        public static ClientsGroup empty(final Integer groupId) {
            return new ClientsGroup(groupId);
        }

    }

    public static class RemoteCallStats {

        private String url;
        private Integer groupId;
        private String requestId;
        private Stopwatch stopwatch;
        private boolean isCompleted = false;

        public RemoteCallStats(final String url,
                               final Integer groupId,
                               final String requestId,
                               final Stopwatch stopwatch,
                               boolean isCompleted) {
            this.url = url;
            this.groupId = groupId;
            this.requestId = requestId;
            this.stopwatch = stopwatch;
            this.isCompleted = isCompleted;
        }

        public Integer getGroupId() {
            return groupId;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public String getUrl() {
            return url;
        }

        public String getRequestId() {
            return requestId;
        }

        public long getTimeInMs(final TimeUnit timeUnit) {
            return stopwatch.elapsed(timeUnit);
        }

    }

}
