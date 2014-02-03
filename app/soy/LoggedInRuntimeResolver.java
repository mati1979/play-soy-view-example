package soy;

import com.github.mati1979.play.soyplugin.global.runtime.RuntimeDataResolver;
import com.google.template.soy.data.SoyMapData;
import play.mvc.Http;

import java.util.Map;

/**
 * Created by mati on 03/02/2014.
 */
public class LoggedInRuntimeResolver implements RuntimeDataResolver {

    @Override
    public void resolveData(Http.Request request, Http.Response response, Map<String, ? extends Object> model, SoyMapData root) {
        root.put("loggedIn", true);
        root.put("email", "mati@sz.home.pl");
    }

}
