package soy;

import com.github.mati1979.play.soyplugin.global.runtime.RuntimeDataResolver;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.google.template.soy.data.SoyMapData;
import controllers.MyAssets;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import play.Logger;
import play.Play;
import play.mvc.Http;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mati on 30/05/2014.
 */
public class AssetsRuntimeResolver implements RuntimeDataResolver {

    private final static Pattern FILE_PATTERN = Pattern.compile(".+/public/(.+)");

    private PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

    private final static Map<String, String> cache = Maps.newConcurrentMap();

    @PostConstruct
    public void init() {
        if (Play.isProd()) {
            Logger.info("creating static assets (md5)...");
            final Stopwatch stopwatch = Stopwatch.createStarted();
            try {
                final Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:public/**/*.md5");
                Arrays.asList(resources).stream()
                        .filter(resource -> resource instanceof UrlResource)
                        .map(resource -> (UrlResource) resource)
                        .forEach(resource -> {
                            final Matcher matcher = FILE_PATTERN.matcher(resource.toString());
                            if (matcher.find()) {
                                final String asset = matcher.group(1).replace(".md5]", "");
                                final String key = "assets." + asset.replace("/", ".");
                                try {
                                    String versioned = MyAssets.versioned(asset);
                                    Logger.info(String.format("key:%s - value:%s", key, versioned));
                                    cache.put(key, versioned);
                                } catch (Exception e) { //swallow
                                }
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException("failed to inject static md5 data", e);
            }
            stopwatch.stop();
            Logger.info("static assets generation took:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
        }
    }

    @Override
    public void resolveData(Http.Request request, Http.Response response, Map<String, ? extends Object> model, SoyMapData root) {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        if (Play.isDev() || Play.isTest()) {
            final File dir = Play.application().getFile("/public/");
            FileUtils.listFiles(dir, null, true).forEach(f -> {
                final Matcher matcher = FILE_PATTERN.matcher(f.getPath());
                if (matcher.find()) {
                    final String asset = matcher.group(1);
                    final String key = "assets." + asset.replace("/", ".");
                    String versioned = MyAssets.versioned(asset);
                    //Logger.info(String.format("key:%s - value:%s", key, versioned));
                    try {
                        root.put(key, versioned);
                    } catch (Exception e) {
                    } //swallow
                }
            });
        }
        if (Play.isProd()) {
            cache.entrySet().stream().forEach(entry -> root.put(entry.getKey(), entry.getValue()));
        }
        Logger.debug("static assets generation took:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
    }

}
