package model;

import pagelets.HeaderModel;
import pagelets.WordsModel;

/**
 * Created by mati on 03/02/2014.
 */
public class IndexPageModel {

    private HeaderModel headerModel;
    private WordsModel wordsModel;

    public IndexPageModel(final HeaderModel headerModel, final WordsModel wordsModel) {
        this.headerModel = headerModel;
        this.wordsModel = wordsModel;
    }

    public WordsModel getWordsModel() {
        return wordsModel;
    }

    public HeaderModel getHeaderModel() {
        return headerModel;
    }

}
