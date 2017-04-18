package uk.co.committedcoding.db;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.committedcoding.api.Todo;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    private List<Todo> getAllOrdered() {
        return list(namedQuery("uk.co.committedcoding.api.Todo.findAll"));
    }

    @UnitOfWork
    public List<Todo> getAll() {
        return getAllOrdered();
    }

    @UnitOfWork
    public Optional<Todo> getById(Long id) {
        return Optional.ofNullable(get(id));
    }

    @UnitOfWork
    public Todo create(@Valid Todo todo) {
        final int priority = getAllOrdered().size(); // zero-based priority so no offset required
        todo.setPriority(priority);
        return persist(todo);
    }

    @UnitOfWork
    public Optional<Todo> update(@Valid Todo updatedTodo) {
        return Optional.ofNullable(get(updatedTodo.getId())).map(existingTodo -> {
            List<Todo> existingList = getAllOrdered();
            Collections.reverse(existingList);

            int existingListSize = existingList.size();

            if (updatedTodo.getPriority() < 0) {
                updatedTodo.setPriority(0);
            }
            if (updatedTodo.getPriority() >= existingListSize) {
                updatedTodo.setPriority(existingListSize-1);
            }

            // remove the existing entry in the list
            existingList.removeIf(todo -> todo.getId().equals(existingTodo.getId()));

            existingTodo.setPriority(updatedTodo.getPriority());
            existingTodo.setDescription(updatedTodo.getDescription());
            existingTodo.setStatus(updatedTodo.getStatus());
            existingTodo.setSummary(updatedTodo.getSummary());
            existingList.add(updatedTodo.getPriority(), existingTodo);

            IntStream.range(0, existingListSize)
                    .forEach(idx -> {
                        Long id = existingList.get(idx).getId();
                        get(id).setPriority(idx);
                    });

            return existingTodo;
        });
    }

    @UnitOfWork
    public void delete(Long id) {
        getById(id).ifPresent(todo -> currentSession().delete(todo));
    }

    @UnitOfWork
    public void drop() {
        currentSession().createQuery("DELETE FROM Todo").executeUpdate();
        currentSession().clear();
    }

}
