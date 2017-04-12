package uk.co.committedcoding.resources;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.committedcoding.TodoApplication;
import uk.co.committedcoding.TodoApplicationConfiguration;
import uk.co.committedcoding.api.Status;
import uk.co.committedcoding.api.Todo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Simon Casey on 10/04/2017.
 */
public class TodoResourceTest extends IntegrationTestSetup<TodoApplicationConfiguration> {

    static final Logger logger = LoggerFactory.getLogger(TodoResourceTest.class);

    @ClassRule
    public static final DropwizardAppRule<TodoApplicationConfiguration> RULE =
            new DropwizardAppRule<TodoApplicationConfiguration>(TodoApplication.class, ResourceHelpers.resourceFilePath("test-todo-dw.yml"));

    DropwizardAppRule<TodoApplicationConfiguration> getRule() {
        return RULE;
    }

    @Before
    public void beforeEach() {
        todoRepository.drop();
    }

    @AfterClass
    public static void tearDown() {
        try {
            Files.deleteIfExists(Paths.get(RULE.getConfiguration().getDbFilePath()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getValidTodo() throws Exception {
        UUID todoId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();

        Todo todo1 = Todo.builder()
                .id(todoId)
                .summary("some summary")
                .listId(listId)
                .build();

        todoRepository.put(todo1);

        HttpGet get = new HttpGet(local("/todo/" + todoId.toString()));

        HttpResponse response = httpClient.execute(get);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_OK);

        Todo result = readContent(response, Todo.class);

        assertThat(result.getId()).isEqualTo(todoId);
        assertThat(result.getListId()).isEqualTo(listId);
        assertThat(result.getSummary()).isEqualTo("some summary");
    }

    @Test
    public void getInvalidTodo() throws Exception {
        Todo todo1 = Todo.builder()
                .summary("some summary")
                .build();

        todoRepository.put(todo1);

        HttpGet get = new HttpGet(local("/todo/" + UUID.randomUUID().toString()));

        HttpResponse response = httpClient.execute(get);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void createValidTodo() throws Exception {
        UUID listId = UUID.randomUUID();

        HttpPost post = new HttpPost(local("/todo"));

        post.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

        JSONObject obj = new JSONObject();
        obj.put("listId", listId.toString());
        obj.put("summary", "test summary");
        obj.put("description", "test description");

        post.setEntity(new StringEntity(obj.toJSONString()));

        HttpResponse response = httpClient.execute(post);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_CREATED);

        Todo result = readContent(response, Todo.class);

        assertThat(result.getListId()).isEqualTo(listId);
        assertThat(result.getSummary()).isEqualTo("test summary");
        assertThat(result.getDescription()).isEqualTo("test description");
        assertThat(response.getFirstHeader(HttpHeaders.LOCATION)).isNotNull();
    }

    @Test
    public void updateValidTodo() throws Exception {
        UUID todoId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();

        Todo todo1 = Todo.builder()
                .id(todoId)
                .summary("some summary")
                .listId(listId)
                .build();

        todoRepository.put(todo1);

        HttpPut put = new HttpPut(local("/todo/" + todoId.toString()));

        put.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

        JSONObject obj = new JSONObject();
        obj.put("id", todoId.toString());
        obj.put("listId", listId.toString());
        obj.put("summary", "updated summary");
        obj.put("description", "updated description");
        obj.put("priority", 10);
        obj.put("status", Status.COMPLETE.getName());

        put.setEntity(new StringEntity(obj.toJSONString()));

        HttpResponse response = httpClient.execute(put);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_OK);

        Todo result = readContent(response, Todo.class);

        assertThat(result.getListId()).isEqualTo(listId);
        assertThat(result.getSummary()).isEqualTo("updated summary");
        assertThat(result.getDescription()).isEqualTo("updated description");
        assertThat(result.getPriority()).isEqualTo(10);
        assertThat(result.getStatus()).isEqualTo(Status.COMPLETE);
    }

    @Test
    public void createInvalidTodo() throws Exception {
        UUID listId = UUID.randomUUID();

        HttpPost post = new HttpPost(local("/todo"));

        post.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

        // missing summary
        JSONObject obj = new JSONObject();
        obj.put("listId", listId.toString());
        obj.put("description", "test description");

        post.setEntity(new StringEntity(obj.toJSONString()));

        HttpResponse response = httpClient.execute(post);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void getValidListTodos() throws Exception {
        UUID listId = UUID.randomUUID();

        Todo todo1 = Todo.builder()
                .summary("some summary 1")
                .listId(listId)
                .build();

        Todo todo2 = Todo.builder()
                .summary("some summary 2")
                .listId(listId)
                .build();

        Todo todo3 = Todo.builder()
                .summary("some summary 3")
                .listId(listId)
                .build();

        todoRepository.put(todo1);
        todoRepository.put(todo2);
        todoRepository.put(todo3);

        HttpGet get = new HttpGet(local("/todo/list/" + listId.toString()));

        HttpResponse response = httpClient.execute(get);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_OK);

        List<Todo> results = Arrays.asList(readContent(response, Todo[].class));

        assertThat(results.size()).isEqualTo(3);
        assertThat(results.stream().anyMatch(todo -> todo.equals(todo1))).isTrue();
        assertThat(results.stream().anyMatch(todo -> todo.equals(todo2))).isTrue();
        assertThat(results.stream().anyMatch(todo -> todo.equals(todo3))).isTrue();
    }

    @Test
    public void getEmptyListTodos() throws Exception {
        UUID listId = UUID.randomUUID();

        Todo todo1 = Todo.builder()
                .summary("some summary 1")
                .listId(listId)
                .build();

        Todo todo2 = Todo.builder()
                .summary("some summary 2")
                .listId(listId)
                .build();

        Todo todo3 = Todo.builder()
                .summary("some summary 3")
                .listId(listId)
                .build();

        todoRepository.put(todo1);
        todoRepository.put(todo2);
        todoRepository.put(todo3);

        HttpGet get = new HttpGet(local("/todo/list/" + UUID.randomUUID().toString()));

        HttpResponse response = httpClient.execute(get);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_OK);

        List<Todo> results = Arrays.asList(readContent(response, Todo[].class));

        assertThat(results.isEmpty()).isTrue();
    }

}
