package uk.co.committedcoding;

import com.google.inject.Provides;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;
import uk.co.committedcoding.db.Database;

/**
 * Created by Simon Casey on 10/04/2017.
 */
public class TodoModule extends DropwizardAwareModule<TodoApplicationConfiguration> {

    @Override
    protected void configure() {
        // anything you'd like to configure
    }

    @Provides
    public Database database() {
        return new Database(configuration().getDbFilePath());
    }

}