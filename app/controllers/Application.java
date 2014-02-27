package controllers;

import actors.MainActor;
import actors.RequestMessage;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.github.mati1979.play.soyplugin.plugin.Soy;
import model.IndexPageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pagelets.HeaderPagelet;
import pagelets.WordsPagelet;
import play.libs.Akka;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

@Service
public class Application extends Controller {

    @Autowired
    private Soy soy;

    @Autowired
    private HeaderPagelet headerPagelet;

    @Autowired
    private WordsPagelet wordsPagelet;

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
                final ActorRef actorRef = Akka.system().actorOf(Props.create(MainActor.class, headerPagelet, wordsPagelet, out, soy));
                Patterns.ask(actorRef, new RequestMessage(request, response), 10000);
            }
        };

        return ok(chunks).as("text/html");
    }

}
