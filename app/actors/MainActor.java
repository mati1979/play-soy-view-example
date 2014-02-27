package actors;

import akka.actor.UntypedActor;
import com.github.mati1979.play.soyplugin.plugin.Soy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import pagelets.HeaderPagelet;
import pagelets.WordsPagelet;

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

    public MainActor() {
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
