package uk.co.committedcoding;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.co.committedcoding.resources.ListResource;
import uk.co.committedcoding.resources.TodoResource;

/**
 * Created by Simon Casey on 07/04/2017.
 */

public class TodoApplication extends Application<TodoApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new TodoApplication().run(args);
    }



    @Override
    public void initialize(Bootstrap<TodoApplicationConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(TodoApplicationConfiguration configuration,
                    Environment environment) {
        // register resources
        final TodoResource todoResource = new TodoResource();
        environment.jersey().register(todoResource);
        final ListResource listResource = new ListResource();
        environment.jersey().register(listResource);
    }
}
