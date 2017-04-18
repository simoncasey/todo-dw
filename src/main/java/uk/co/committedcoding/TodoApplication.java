package uk.co.committedcoding;

import com.github.dirkraft.dropwizard.fileassets.FileAssetsBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import uk.co.committedcoding.api.Todo;

/**
 * Created by Simon Casey on 07/04/2017.
 */

public class TodoApplication extends Application<TodoApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new TodoApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<TodoApplicationConfiguration> bootstrap) {
        final TodoHibernateBundle todoHibernateBundle = new TodoHibernateBundle();
        bootstrap.addBundle(todoHibernateBundle);
        bootstrap.addBundle(GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                .modules(new TodoModule(todoHibernateBundle))
                .build());
        bootstrap.addBundle(new ViewBundle<>());
//        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
        bootstrap.addBundle(new FileAssetsBundle("src/main/resources/assets", "/", "index.html"));
    }

    @Override
    public void run(TodoApplicationConfiguration configuration,
                    Environment environment) {
    }
}
