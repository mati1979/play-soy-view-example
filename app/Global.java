import soy.PlaySoyConfigExt;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import play.Application;
import play.GlobalSettings;

/**
 * Created by mati on 02/02/2014.
 */
public class Global extends GlobalSettings {

    private AnnotationConfigApplicationContext ctx;

    @Override
    public void onStart(final Application app) {
        this.ctx = new AnnotationConfigApplicationContext();
        this.ctx.register(PlaySoyConfigExt.class);
        this.ctx.scan("controllers", "pagelets");
        this.ctx.refresh();
    }

    @Override
    public <A> A getControllerInstance(final Class<A> clazz) {
        return ctx.getBean(clazz);
    }

}

