package uk.co.committedcoding;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;

import java.io.File;

/**
 * Created by Simon Casey on 07/04/2017.
 */

public class TodoApplication extends Application<TodoApplicationConfiguration> {

//    private GuiceBundle<TodoApplicationConfiguration> guiceBundle;

    public static void main(String[] args) throws Exception {
        new TodoApplication().run(args);
    }


    @Override
    public void initialize(Bootstrap<TodoApplicationConfiguration> bootstrap) {
        bootstrap.addBundle(GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                .modules(new TodoModule())
                .build());
//        guiceBundle = GuiceBundle.<TodoApplicationConfiguration>newBuilder()
//                .addModule(new TodoModule())
//                .enableAutoConfig(getClass().getPackage().getName())
//                .setConfigClass(TodoApplicationConfiguration.class)
//                .build();
//
//        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(TodoApplicationConfiguration configuration,
                    Environment environment) {
    }
}
