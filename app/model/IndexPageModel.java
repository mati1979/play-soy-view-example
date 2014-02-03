package model;

import model.pagelets.HeaderModel;
import model.pagelets.WordsModel;

/**
 * Created by mati on 03/02/2014.
 */
public class IndexPageModel {

    private HeaderModel headerModel;
    private WordsModel wordsModel;

    public WordsModel getWordsModel() {
        return wordsModel;
    }

    public void setWordsModel(final WordsModel wordsModel) {
        this.wordsModel = wordsModel;
    }

    public void setHeaderModel(HeaderModel headerModel) {
        this.headerModel = headerModel;
    }

    public HeaderModel getHeaderModel() {
        return headerModel;
    }

}
