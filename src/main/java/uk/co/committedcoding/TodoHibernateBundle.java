package uk.co.committedcoding;

import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import uk.co.committedcoding.api.Todo;

/**
 * Created by Simon Casey on 18/04/2017.
 */
public class TodoHibernateBundle extends HibernateBundle<TodoApplicationConfiguration> {

    public TodoHibernateBundle() {
        super(Todo.class);
    }

    @Override
    public PooledDataSourceFactory getDataSourceFactory(TodoApplicationConfiguration configuration) {
        return configuration.getDataSourceFactory();
    }
}