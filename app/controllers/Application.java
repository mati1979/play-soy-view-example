package controllers;

import com.github.mati1979.play.soyplugin.soy.Soy;
import model.IndexPageModel;
import pagelets.HeaderModel;
import pagelets.HeaderPagelet;
import pagelets.WordsModel;
import pagelets.WordsPagelet;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import utils.Promises;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Application extends Controller {

    @Inject
    private Soy soy;

    @Inject
    private HeaderPagelet headerPagelet;

    @Inject
    private WordsPagelet wordsPagelet;

    public F.Promise<Result> index() {
        final F.Promise<HeaderModel> headerModelP = headerPagelet.invoke();
        final F.Promise<WordsModel> wordsModelP = wordsPagelet.invoke();

        return Promises.zip(headerModelP, wordsModelP, (header, words)
                -> ok(Html.apply(soy.html("pages.index", new IndexPageModel(header, words)))));
    }

}
