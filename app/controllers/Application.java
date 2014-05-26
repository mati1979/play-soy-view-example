package controllers;

import com.github.mati1979.play.soyplugin.plugin.Soy;
import model.IndexPageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pagelets.HeaderModel;
import pagelets.HeaderPagelet;
import pagelets.WordsModel;
import pagelets.WordsPagelet;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;

@Component
public class Application extends Controller {

    @Autowired
    private Soy soy;

    @Autowired
    private HeaderPagelet headerPagelet;

    @Autowired
    private WordsPagelet wordsPagelet;

    public F.Promise<Result> index() {
        final F.Promise<HeaderModel> headerModelP = headerPagelet.invoke();
        final F.Promise<WordsModel> wordsModelP = wordsPagelet.invoke();

        return headerModelP.flatMap(headerModel -> wordsModelP
                .map(wordsModel -> ok(Html.apply(soy.html("pages.index", new IndexPageModel(headerModel, wordsModel))))));
    }

}
