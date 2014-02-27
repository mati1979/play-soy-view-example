package controllers;

import actors.RequestMessage;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.github.mati1979.play.soyplugin.plugin.Soy;
import helper.SpringExtension;
import model.IndexPageModel;
import org.springframework.beans.factory.annotation.Autowired;
import pagelets.HeaderPagelet;
import pagelets.WordsPagelet;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

@org.springframework.stereotype.Controller
public class Application extends Controller {

    @Autowired
    private Soy soy;

    @Autowired
    private HeaderPagelet headerPagelet;

    @Autowired
    private WordsPagelet wordsPagelet;

    @Autowired
    private ActorSystem system;

    public Result normalRender() throws Exception {
        final IndexPageModel indexPageModel = new IndexPageModel();
        indexPageModel.setHeaderModel(headerPagelet.invoke());
        indexPageModel.setWordsModel(wordsPagelet.invoke());

        Thread.sleep(4000);

        return ok(soy.html("pages.index", indexPageModel));
    }

    public Result progressiveRender() throws Exception {
        final Http.Request request = request();
        final Http.Response response = response();

        final Chunks<String> chunks = new StringChunks() {

            @Override
            public void onReady(final Out<String> out) {
                createMainActor().tell(new RequestMessage(request, response, out), null);
            }

        };

        return ok(chunks).as("text/html");
    }

    private ActorRef createMainActor() {
        return system.actorOf(SpringExtension.SpringExtProvider.get(system).props("MainActor"), "mainActor" + System.currentTimeMillis());
    }

}
