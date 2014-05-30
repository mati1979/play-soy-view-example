package pagelets;

import org.springframework.stereotype.Service;
import play.libs.F;

/**
 * Created by mati on 03/02/2014.
 */
@Service
public class HeaderPagelet {

    public F.Promise<HeaderModel> invoke() {

        final HeaderModel headerModel = new HeaderModel();
        headerModel.setTitle("Test Title");

        return F.Promise.pure(headerModel);
    }

}
