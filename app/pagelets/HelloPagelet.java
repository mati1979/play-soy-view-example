package pagelets;

import org.springframework.stereotype.Service;
import react.Try;

/**
 * Created by mati on 03/02/2014.
 */
@Service
public class HelloPagelet {

    public Try<String> invoke() {
        if (new java.util.Random().nextBoolean()) {
            return Try.success("hello");
        }

        return Try.failure(new Exception());
    }

}
