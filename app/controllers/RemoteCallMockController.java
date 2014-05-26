package controllers;

import org.springframework.stereotype.Component;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Random;

/**
 * Created by mati on 26/05/2014.
 */
@Component
public class RemoteCallMockController extends Controller {

    public F.Promise<Result> index() {
        System.out.println("********remote call********:" + request().remoteAddress());
        System.out.println("remote:" + request().queryString());

        try {
            final int val = 1000 + new Random().nextInt(10) * 1000;
            System.out.println("val:" + val);
            Thread.sleep(val);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return F.Promise.pure(ok("NA DU?"));
    }

}
