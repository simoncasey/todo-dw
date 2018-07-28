package uk.co.committedcoding.service;

import io.dropwizard.hibernate.UnitOfWork;
import javaslang.control.Option;
import uk.co.committedcoding.api.Todo;
import uk.co.committedcoding.db.TodoRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Simon Casey on 24/04/2017.
 */
public class TodoService {

    private TodoRepository todoRepository;

    @Inject
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    protected Todo update(Todo existingTodo, Todo updatedTodo) {
        List<Todo> existingList = todoRepository.getAll();

        int existingListSize = existingList.size();

        if (updatedTodo.getPriority() < 0) {
            updatedTodo.setPriority(0);
        }
        if (updatedTodo.getPriority() >= existingListSize) {
            updatedTodo.setPriority(existingListSize-1);
        }

        existingTodo.setPriority(updatedTodo.getPriority());
        existingTodo.setDescription(updatedTodo.getDescription());
        existingTodo.setStatus(updatedTodo.getStatus());
        existingTodo.setSummary(updatedTodo.getSummary());

        if (!existingTodo.getPriority().equals(updatedTodo.getPriority())) {
            Collections.sort(existingList);
            // remove the existing entry in the list
            existingList.removeIf(todo -> todo.getId().equals(existingTodo.getId()));
            existingList.add(updatedTodo.getPriority(), existingTodo);
            // re-index the priorities of all items
            IntStream.range(0, existingListSize)
                    .forEach(idx -> existingList.get(idx).setPriority(idx));
        }
        return existingTodo;
    }

    protected Todo create(Todo newTodo) {
        final int priority = todoRepository.getAll().size(); // zero-based priority so no offset required
        newTodo.setPriority(priority);
        return todoRepository.save(newTodo);
    }

    @UnitOfWork
    public Todo persist(@Valid Todo updatedTodo) {
        Option<Long> providedId = Option.of(updatedTodo.getId());

        Option<Todo> existingTodo = providedId.flatMap(existingId -> todoRepository.getById(existingId));

        return existingTodo.map(et -> update(et, updatedTodo))
                .getOrElse(create(updatedTodo));
    }

    @UnitOfWork
    public void delete(Long id) {
        todoRepository.getById(id).forEach( todo -> todoRepository.delete(todo));
    }
}
