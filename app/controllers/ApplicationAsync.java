package controllers;

import actors.RequestMessage;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import helper.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import play.api.templates.Html;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

/**
 * Created by mszczap on 27.02.14.
 */
@org.springframework.stereotype.Controller
public class ApplicationAsync extends Controller {

    @Autowired
    private ActorSystem system;

    public Result index() throws Exception {
        final Http.Request request = request();
        final Http.Response response = response();

        final Results.Chunks<String> chunks = new Results.StringChunks() {

            @Override
            public void onReady(final Out<String> out) {
                createMainActor().tell(new RequestMessage(request, response, out), null);
            }

        };

        return ok(chunks).as(Html.empty().contentType());
    }

    private ActorRef createMainActor() {
        return system.actorOf(SpringExtension.SpringExtProvider.get(system).props("MainActor"), "mainActor" + System.currentTimeMillis());
    }

}
