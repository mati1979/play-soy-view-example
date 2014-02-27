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
@ComponentScan({"controllers", "pagelets"})
public class PlaySoyViewExampleConfig {

//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @Bean
//    public ActorSystem actorSystem() {
//        ActorSystem system = Akka.system();
//        // initialize the application context in the Akka Spring Extension
//        SpringExtProvider.get(system).initialize(applicationContext);
//
//        return system;
//    }

}
