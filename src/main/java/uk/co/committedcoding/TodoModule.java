package uk.co.committedcoding;

import org.hibernate.SessionFactory;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

/**
 * Created by Simon Casey on 10/04/2017.
 */
public class TodoModule extends DropwizardAwareModule<TodoApplicationConfiguration> {

    private final TodoHibernateBundle todoHibernateBundle;

    public TodoModule(TodoHibernateBundle todoHibernateBundle) {
        this.todoHibernateBundle = todoHibernateBundle;
    }

    @Override
    protected void configure() {
        bind(SessionFactory.class).toInstance(todoHibernateBundle.getSessionFactory());
    }

}