package pagelets;

import model.pagelets.HeaderModel;
import org.springframework.stereotype.Service;

/**
 * Created by mati on 03/02/2014.
 */
@Service
public class HeaderPagelet {

    public HeaderModel invoke() {
        final HeaderModel headerModel = new HeaderModel();
        headerModel.setSoyUtilsLink("/assets/bower_components/soyutils/soyutils.js");
        headerModel.setjQueryLink("/assets/bower_components/jquery/jquery.js");
        headerModel.setTitle("Test Title");

        return headerModel;
    }

}
