package controllers;

import actors.MainActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.github.mati1979.play.soyplugin.config.PlayConfAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import play.api.templates.Html;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import scala.io.Codec;

/**
 * Created by mszczap on 27.02.14.
 */
@org.springframework.stereotype.Controller
public class ApplicationAsync extends Controller {

    @Autowired
    private ActorSystem system;

    public Result index() throws Exception {
        final Results.Chunks<String> dataChunks = chunks(request(), response());

        return ok(dataChunks).as(Html.empty().contentType() + ";charset=" + PlayConfAccessor.GLOBAL_ENCODING);
    }

    private Results.Chunks<String> chunks(final Http.Request request, final Http.Response response) {
        return new Results.StringChunks(PlayConfAccessor.GLOBAL_ENCODING) {

            @Override
            public void onReady(final Out<String> out) {
                final ActorRef actorRef = MainActor.create(system);
                actorRef.tell(new MainActor.RequestMessage(request, response, out), null);
            }

        };
    }

}
