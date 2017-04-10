package uk.co.committedcoding;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import uk.co.committedcoding.db.Database;

/**
 * Created by Simon Casey on 10/04/2017.
 */
public class TodoModule extends AbstractModule {
    @Override
    protected void configure() {
        // anything you'd like to configure
    }

    @Provides
    public Database database(TodoApplicationConfiguration configuration) {
        return new Database(configuration.getDbFilePath());
    }

}