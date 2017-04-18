package uk.co.committedcoding.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vyarus.dropwizard.guice.module.installer.feature.eager.EagerSingleton;
import uk.co.committedcoding.api.Todo;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by Simon Casey on 10/04/2017.
 */
@EagerSingleton
public class TodoRepository extends AbstractDAO<Todo> {

    static final Logger logger = LoggerFactory.getLogger(TodoRepository.class);

    @Inject
    public TodoRepository(final SessionFactory factory) {
        super(factory);
    }

    public List<Todo> getAll() {
        return list(namedQuery("uk.co.committedcoding.api.Todo.findAll"));
    }

    public Optional<Todo> getById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Todo create(@Valid Todo todo) {
        return persist(todo);
    }

    public Optional<Todo> update(@Valid Todo todo) {
        return getById(todo.getId()).map( existing -> {
            existing.setPriority(todo.getPriority());
            existing.setDescription(todo.getDescription());
            existing.setStatus(todo.getStatus());
            existing.setSummary(todo.getSummary());
            return existing;
        });
    }

    public void delete(Long id) {
        delete(id);
    }

}
