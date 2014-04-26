package spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import soy.PlaySoyConfigExt;

/**
 * Created by mati on 22/02/2014.
 */
@Configuration
@Import(PlaySoyConfigExt.class)
@ComponentScan({"controllers", "pagelets", "actors"})
public class PlaySoyViewExampleConfig {

}
