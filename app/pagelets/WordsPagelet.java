package pagelets;

import com.google.common.collect.Lists;
import model.pagelets.WordsModel;
import org.springframework.stereotype.Service;
import play.libs.F;
import play.libs.WS;

/**
 * Created by mati on 03/02/2014.
 */
@Service
public class WordsPagelet {

    public F.Promise<WordsModel> invoke() throws InterruptedException {
        final F.Promise<WS.Response> responsePromise = WS.url("http://www.onet.eu").get();

        return responsePromise.map(new F.Function<WS.Response, WordsModel>() {
            @Override
            public WordsModel apply(WS.Response response) throws Throwable {
                return new WordsModel(Lists.newArrayList("test", "test2"));
            }
        });
    }

}
