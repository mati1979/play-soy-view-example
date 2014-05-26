package controllers;

import hysterix.HelloCommandNoFallback;
import hysterix.HelloFallbackCommand;
import hysterix.HystrixRequestCache;
import org.springframework.stereotype.Component;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.concurrent.TimeUnit;

@Component
public class HystrixController extends Controller {

    private static final HystrixRequestCache httpRequestsCache = new HystrixRequestCache();

    public F.Promise<Result> helloWithFallback() {
        final HelloFallbackCommand helloFallbackCommand = new HelloFallbackCommand(httpRequestsCache);
        System.out.println("cache.size:" + httpRequestsCache.size());

        return helloFallbackCommand.execute().map(s -> onSuccess(s, helloFallbackCommand));
    }

    public F.Promise<Result> helloWithoutFallback() {
        final HelloCommandNoFallback helloFallbackCommand = new HelloCommandNoFallback(httpRequestsCache);

        return helloFallbackCommand.execute().map(s -> onSuccess(s, helloFallbackCommand));
    }

    public F.Promise<Result> clearCache() {
        httpRequestsCache.clear();

        return F.Promise.pure(ok("cleared"));
    }

    public Result onSuccess(final String s, HelloFallbackCommand helloFallbackCommand) {
        System.out.println(helloFallbackCommand.getExecutionEvents());
        System.out.println("isComplete:" + helloFallbackCommand.isExecutionComplete());
        System.out.println("executionTime:" + helloFallbackCommand.getExecutionTime(TimeUnit.MILLISECONDS));

        return ok(s);
    }

    public Result onSuccess(final String s, HelloCommandNoFallback helloFallbackCommand) {
        System.out.println("executionEvents:" + helloFallbackCommand.getExecutionEvents());
        System.out.println("isComplete:" + helloFallbackCommand.isExecutionComplete());
        System.out.println("executionTime:" + helloFallbackCommand.getExecutionTime(TimeUnit.MILLISECONDS));

        return ok(s);
    }

}
