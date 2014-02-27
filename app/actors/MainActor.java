package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import com.github.mati1979.play.soyplugin.plugin.Soy;
import helper.SpringExtension;
import model.pagelets.WordsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import pagelets.HeaderPagelet;
import pagelets.WordsPagelet;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Results;

import javax.inject.Named;

/**
 * Created by mszczap on 27.02.14.
 */
@Named("MainActor")
@Scope("prototype")
public class MainActor extends UntypedActor {

    @Autowired
    private HeaderPagelet headerPagelet;

    @Autowired
    private WordsPagelet wordsPagelet;

    @Autowired
    private Soy soy;

    @Override
    public void onReceive(final Object message) throws Exception {
        final RequestMessage req = (RequestMessage) message;
        req.getOut().write(soy.html(req.getRequest(), req.getResponse(), "pods.head.index", headerPagelet.invoke()));
        req.getOut().write(soy.html(req.getRequest(), req.getResponse(), "pods.header.index"));

        wordsPagelet.invoke().recover(new F.Function<Throwable, WordsModel>() {
            @Override
            public WordsModel apply(Throwable throwable) throws Throwable {
                req.getOut().close();
                return null;
            }
        }).onRedeem(new F.Callback<WordsModel>() {
            @Override
            public void invoke(final WordsModel wordsModel) throws Throwable {
                req.getOut().write(soy.html(req.getRequest(), req.getResponse(), "pods.main.index", wordsModel));
                req.getOut().write(soy.html(req.getRequest(), req.getResponse(), "pods.footer.index"));
                req.getOut().close();
            }
        });
    }

    public static ActorRef create(final ActorSystem system) {
        return system.actorOf(SpringExtension.SpringExtProvider.get(system).props("MainActor"), "mainActor" + System.currentTimeMillis());
    }

    public static class RequestMessage {

        private Http.Request request;
        private Http.Response response;
        private Results.Chunks.Out<String> out;

        public RequestMessage(Http.Request request, Http.Response response, Results.Chunks.Out<String> out) {
            this.request = request;
            this.response = response;
            this.out = out;
        }

        public Results.Chunks.Out<String> getOut() {
            return out;
        }

        public Http.Request getRequest() {
            return request;
        }

        public Http.Response getResponse() {
            return response;
        }

    }


}
