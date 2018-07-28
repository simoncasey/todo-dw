package uk.co.committedcoding.db;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.hibernate.UnitOfWork;
import javaslang.control.Option;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.committedcoding.api.Todo;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Simon Casey on 10/04/2017.
 */
@Singleton
public class TodoRepository extends AbstractDAO<Todo> {

    static final Logger logger = LoggerFactory.getLogger(TodoRepository.class);

    @Inject
    public TodoRepository(final SessionFactory factory) {
        super(factory);
    }

    @UnitOfWork
    public List<Todo> getAll() {
        return list(query("FROM Todo t"));
    }

    @UnitOfWork
    public Option<Todo> getById(Long id) {
        return Option.of(get(id));
    }

    @UnitOfWork
    public Todo save(@Valid Todo todo) {
        return persist(todo);
    }

    @UnitOfWork
    public void delete(Todo todo) {
        currentSession().delete(todo);
    }

    @UnitOfWork
    public void drop() {
        currentSession().createQuery("DELETE FROM Todo").executeUpdate();
        currentSession().clear();
    }

}
