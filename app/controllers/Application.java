package controllers;

import com.github.mati1979.play.soyplugin.plugin.Soy;
import model.IndexPageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pagelets.HeaderPagelet;
import pagelets.WordsPagelet;
import play.mvc.Controller;
import play.mvc.Result;

@Service
public class Application extends Controller {

    @Autowired
    private Soy soy;

    @Autowired
    private WordsPagelet wordsPagelet;

    @Autowired
    private HeaderPagelet headerPagelet;

    public Result index() throws Exception {
        final IndexPageModel indexPageModel = new IndexPageModel();
        indexPageModel.setHeaderModel(headerPagelet.invoke());
        indexPageModel.setWordsModel(wordsPagelet.invoke());

        return ok(soy.html("pages.index", indexPageModel));
    }

}
