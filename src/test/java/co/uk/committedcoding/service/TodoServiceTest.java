package co.uk.committedcoding.service;

import javaslang.control.Option;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.co.committedcoding.api.Todo;
import uk.co.committedcoding.db.TodoRepository;
import uk.co.committedcoding.service.TodoService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Simon Casey on 24/04/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository mockTodoRepository;

    @Test
    public void testUpdateNewTodo() throws Exception {
        Todo testTodo1 = Todo.builder()
                .summary("test summary")
                .build();

        when(mockTodoRepository.getAll()).thenReturn(Collections.emptyList());
        Todo savedTodo = Todo.builder()
                .id(1L)
                .summary("test summary")
                .build();
        when(mockTodoRepository.save(testTodo1)).thenReturn(savedTodo);

        TodoService todoService = new TodoService(mockTodoRepository);
        Todo result = todoService.persist(testTodo1);
        assertThat(result.getSummary()).isEqualTo(testTodo1.getSummary());
    }

    @Test
    public void testUpdateExistingTodo() throws Exception {
        Todo existingTodo1 = Todo.builder()
                .id(1L)
                .summary("test summary")
                .priority(0)
                .build();
        Todo existingTodo2 = Todo.builder()
                .id(2L)
                .summary("test summary")
                .priority(1)
                .build();

        when(mockTodoRepository.getById(existingTodo1.getId())).thenReturn(Option.some(existingTodo1));
        List<Todo> allTodos = new ArrayList<>();
        allTodos.add(existingTodo1);
        allTodos.add(existingTodo2);
        when(mockTodoRepository.getAll()).thenReturn(allTodos);

        Todo updatedTodo1 = Todo.builder()
                .id(1L)
                .summary("updated test summary")
                .priority(13)
                .build();

        Todo expectedTodo = Todo.builder()
                .id(1L)
                .summary("updated test summary")
                .priority(1)
                .build();

        TodoService todoService = new TodoService(mockTodoRepository);
        Todo result = todoService.persist(updatedTodo1);
        assertThat(result).isEqualTo(expectedTodo);
        assertThat(result.getSummary()).isEqualTo("updated test summary");
        assertThat(result.getPriority()).isEqualTo(1);
    }
}
