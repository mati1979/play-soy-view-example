package pagelets;

import play.libs.F;

import javax.inject.Singleton;

/**
 * Created by mati on 03/02/2014.
 */
@Singleton
public class HeaderPagelet {

    public F.Promise<HeaderModel> invoke() {

        final HeaderModel headerModel = new HeaderModel();
        headerModel.setTitle("Test Title");

        return F.Promise.pure(headerModel);
    }

}
