package pagelets;

import com.google.common.collect.Lists;
import pagelets.WordsModel;
import org.springframework.stereotype.Service;
import play.libs.F;

/**
 * Created by mati on 03/02/2014.
 */
@Service
public class WordsPagelet {

    public F.Promise<WordsModel> invoke() {
        return F.Promise.pure(new WordsModel(Lists.newArrayList("test", "test2")));
    }

}
