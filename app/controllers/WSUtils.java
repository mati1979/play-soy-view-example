package controllers;

import com.google.common.base.Objects;
import play.libs.ws.WSRequestHolder;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Created by mati on 24/05/2014.
 */
public class WSUtils {

    public static String toUrl(final WSRequestHolder holder) {
        final String url = holder.getUrl();
        final Map<String, Collection<String>> queryParameters = holder.getQueryParameters();

        final String params = queryParameters.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + paramValueToArray(entry.getValue()).orElse(""))
                .reduce((entry1, entry2) -> "&" + entry1 + entry2).orElse("");

        return params.isEmpty()? url : url.concat("?").concat(params);
    }

    public static int hashCode(final WSRequestHolder holder) {
        return Objects.hashCode(holder.getUrl(), holder.getQueryParameters(), holder.getHeaders());
    }

    private static Optional<String> paramValueToArray(final Collection<String> values) {
        return values.stream().reduce((param1, param2) -> param1 + "," + param2);
    }

}
