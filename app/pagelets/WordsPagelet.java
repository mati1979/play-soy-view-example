package pagelets;

import com.google.common.collect.Lists;
import model.pagelets.WordsModel;
import org.springframework.stereotype.Service;

/**
 * Created by mati on 03/02/2014.
 */
@Service
public class WordsPagelet {

    public WordsModel invoke() {
        return new WordsModel(Lists.newArrayList("test", "test2"));
    }

}
