package actors;

import akka.actor.UntypedActor;
import com.github.mati1979.play.soyplugin.plugin.Soy;
import pagelets.HeaderPagelet;
import pagelets.WordsPagelet;
import play.mvc.Results;

/**
 * Created by mszczap on 27.02.14.
 */
public class MainActor extends UntypedActor {

    private HeaderPagelet headerPagelet;

    private WordsPagelet wordsPagelet;

    private Soy soy;

    private Results.Chunks.Out<String> out;

    public MainActor(final HeaderPagelet headerPagelet, final WordsPagelet wordsPagelet, final Results.Chunks.Out<String> out, final Soy soy) {
        this.headerPagelet = headerPagelet;
        this.wordsPagelet = wordsPagelet;
        this.out = out;
        this.soy = soy;
    }

    @Override
    public void onReceive(final Object message) throws Exception {
        RequestMessage req = (RequestMessage) message;
        out.write(soy.html(req.getRequest(), req.getResponse(), "pods.head.index", headerPagelet.invoke()));
        out.write(soy.html(req.getRequest(), req.getResponse(), "pods.header.index"));
        out.write(soy.html(req.getRequest(), req.getResponse(), "pods.main.index", wordsPagelet.invoke()));
        out.write(soy.html(req.getRequest(), req.getResponse(), "pods.footer.index"));
        out.close();
    }

}
