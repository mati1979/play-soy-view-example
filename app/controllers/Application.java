package controllers;

import com.github.mati1979.play.soyplugin.config.PlayConfAccessor;
import com.github.mati1979.play.soyplugin.plugin.Soy;
import model.IndexPageModel;
import org.springframework.beans.factory.annotation.Autowired;
import pagelets.HeaderPagelet;
import pagelets.WordsPagelet;
import play.mvc.Controller;
import play.mvc.Result;

@org.springframework.stereotype.Controller
public class Application extends Controller {

    @Autowired
    private Soy soy;

    @Autowired
    private HeaderPagelet headerPagelet;

    @Autowired
    private WordsPagelet wordsPagelet;

    public Result index() throws Exception {
        final IndexPageModel indexPageModel = new IndexPageModel();
        indexPageModel.setHeaderModel(headerPagelet.invoke());
        indexPageModel.setWordsModel(wordsPagelet.invoke());

        Thread.sleep(4000);

        return ok(soy.html("pages.index", indexPageModel), PlayConfAccessor.GLOBAL_ENCODING);
    }

}
