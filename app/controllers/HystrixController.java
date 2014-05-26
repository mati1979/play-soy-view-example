package controllers;

import hysterix.*;
import org.springframework.stereotype.Component;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.concurrent.TimeUnit;

@Component
public class HystrixController extends Controller {

    private static final HystrixRequestCache httpRequestsCache = new HystrixRequestCache();
    private static final HysterixRequestLog requestLog = new HysterixRequestLog();
    private static final HysterixSettings settings = new HysterixSettings();

    public F.Promise<Result> helloWithFallback() {
        final HelloFallbackCommand helloFallbackCommand = new HelloFallbackCommand(httpRequestsCache, requestLog, settings);
        System.out.println("cache.size:" + httpRequestsCache.size());

        return helloFallbackCommand.execute().map(s -> onSuccess(s, helloFallbackCommand, requestLog));
    }

    public F.Promise<Result> helloWithoutFallback() {
        final HelloCommandNoFallback helloFallbackCommand = new HelloCommandNoFallback(httpRequestsCache, requestLog, settings);

        return helloFallbackCommand.execute().map(s -> onSuccess(s, helloFallbackCommand, requestLog));
    }

    public F.Promise<Result> clearCache() {
        httpRequestsCache.clear();

        return F.Promise.pure(ok("cleared"));
    }

    public Result onSuccess(final String s, HelloFallbackCommand helloFallbackCommand, final HysterixRequestLog hysterixRequestLog) {
        System.out.println(helloFallbackCommand.getExecutionEvents());
        System.out.println("isComplete:" + helloFallbackCommand.isExecutionComplete());
        System.out.println("executionTime:" + helloFallbackCommand.getExecutionTime(TimeUnit.MILLISECONDS));

        System.out.println(hysterixRequestLog.getExecutedCommandsAsString());

        return ok(s);
    }

    public Result onSuccess(final String s, HelloCommandNoFallback helloFallbackCommand, final HysterixRequestLog hysterixRequestLog) {
        System.out.println("executionEvents:" + helloFallbackCommand.getExecutionEvents());
        System.out.println("isComplete:" + helloFallbackCommand.isExecutionComplete());
        System.out.println("executionTime:" + helloFallbackCommand.getExecutionTime(TimeUnit.MILLISECONDS));

        System.out.println(hysterixRequestLog.getExecutedCommandsAsString());

        return ok(s);
    }

}
