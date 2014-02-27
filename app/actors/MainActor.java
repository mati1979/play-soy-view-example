package actors;

import akka.actor.UntypedActor;
import com.github.mati1979.play.soyplugin.plugin.Soy;
import pagelets.HeaderPagelet;
import pagelets.WordsPagelet;

/**
 * Created by mszczap on 27.02.14.
 */
public class MainActor extends UntypedActor {

    private HeaderPagelet headerPagelet;

    private WordsPagelet wordsPagelet;

    private Soy soy;

    public MainActor(final HeaderPagelet headerPagelet, final WordsPagelet wordsPagelet, final Soy soy) {
        this.headerPagelet = headerPagelet;
        this.wordsPagelet = wordsPagelet;
        this.soy = soy;
    }

    @Override
    public void onReceive(final Object message) throws Exception {
        final RequestMessage req = (RequestMessage) message;
        req.getOut().write(soy.html(req.getRequest(), req.getResponse(), "pods.head.index", headerPagelet.invoke()));
        req.getOut().write(soy.html(req.getRequest(), req.getResponse(), "pods.header.index"));
        req.getOut().write(soy.html(req.getRequest(), req.getResponse(), "pods.main.index", wordsPagelet.invoke()));
        req.getOut().write(soy.html(req.getRequest(), req.getResponse(), "pods.footer.index"));
        req.getOut().close();
    }

}
