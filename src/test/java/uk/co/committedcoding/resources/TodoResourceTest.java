package uk.co.committedcoding.resources;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DAOTestRule;
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
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.committedcoding.TodoApplication;
import uk.co.committedcoding.TodoApplicationConfiguration;
import uk.co.committedcoding.api.Status;
import uk.co.committedcoding.api.Todo;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Simon Casey on 10/04/2017.
 */
public class TodoResourceTest extends IntegrationTestSetup<TodoApplicationConfiguration> {

    static final Logger logger = LoggerFactory.getLogger(TodoResourceTest.class);

    @ClassRule
    public static final DropwizardAppRule<TodoApplicationConfiguration> RULE =
            new DropwizardAppRule<>(TodoApplication.class, ResourceHelpers.resourceFilePath("test-todo-dw.yml"));

    DropwizardAppRule<TodoApplicationConfiguration> getRule() {
        return RULE;
    }

    @Before
    public void beforeEach() {
        todoRepository.drop();
    }

    @AfterClass
    public static void tearDown() {
    }

    @Test
    public void getValidTodo() throws Exception {

        Todo todo1 = Todo.builder()
                .summary("some summary")
                .build();

        final Long todoId = todoRepository.save(todo1).getId();

        HttpGet get = new HttpGet(local("/todo/" + todoId));

        HttpResponse response = httpClient.execute(get);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_OK);

        Todo result = readContent(response, Todo.class);

        assertThat(result.getId()).isEqualTo(todoId);
        assertThat(result.getSummary()).isEqualTo("some summary");
    }

    @Test
    public void getInvalidTodo() throws Exception {
        HttpGet get = new HttpGet(local("/todo/" + 999));

        HttpResponse response = httpClient.execute(get);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void createValidTodo() throws Exception {

        HttpPost post = new HttpPost(local("/todo"));

        post.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

        JSONObject obj = new JSONObject();
        obj.put("summary", "test summary");
        obj.put("description", "test description");

        post.setEntity(new StringEntity(obj.toJSONString()));

        HttpResponse response = httpClient.execute(post);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_CREATED);

        Todo result = readContent(response, Todo.class);

        assertThat(result.getSummary()).isEqualTo("test summary");
        assertThat(result.getDescription()).isEqualTo("test description");
        assertThat(response.getFirstHeader(HttpHeaders.LOCATION)).isNotNull();
    }

    @Test
    public void updateValidTodo() throws Exception {

        Todo todo1 = Todo.builder()
                .summary("some summary")
                .build();

        final Long todoId = todoRepository.save(todo1).getId();

        HttpPut put = new HttpPut(local("/todo/" + todoId.toString()));

        put.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

        JSONObject obj = new JSONObject();
        obj.put("id", todoId.toString());
        obj.put("summary", "updated summary");
        obj.put("description", "updated description");
        obj.put("priority", 0);
        obj.put("status", Status.COMPLETE.getName());

        put.setEntity(new StringEntity(obj.toJSONString()));

        HttpResponse response = httpClient.execute(put);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_OK);

        Todo result = readContent(response, Todo.class);

        assertThat(result.getSummary()).isEqualTo("updated summary");
        assertThat(result.getDescription()).isEqualTo("updated description");
        assertThat(result.getPriority()).isEqualTo(0);
        assertThat(result.getStatus()).isEqualTo(Status.COMPLETE);
    }

    @Test
    public void updatePriority() throws Exception {

        Todo todo1 = Todo.builder()
                .priority(0)
                .summary("i was first")
                .build();
        Todo todo2 = Todo.builder()
                .priority(1)
                .summary("i was second")
                .build();
        Todo todo3 = Todo.builder()
                .priority(2)
                .summary("i was third")
                .build();

        final Long todoId = todoRepository.save(todo1).getId();
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        HttpGet get = new HttpGet(local("/todo/" + todoId.toString()));
        HttpResponse getResponse = httpClient.execute(get);
        Todo getResult = readContent(getResponse, Todo.class);
        assertThat(getResult.getPriority()).isEqualTo(0);

        HttpPut put = new HttpPut(local("/todo/" + todoId.toString()));
        put.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

        JSONObject obj = new JSONObject();
        obj.put("id", todoId.toString());
        obj.put("summary", "now i am second");
        obj.put("description", "updated description");
        obj.put("priority", 1);
        obj.put("status", Status.COMPLETE.getName());

        put.setEntity(new StringEntity(obj.toJSONString()));

        HttpResponse response = httpClient.execute(put);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_OK);

        Todo result = readContent(response, Todo.class);

        assertThat(result.getSummary()).isEqualTo("now i am second");
        assertThat(result.getDescription()).isEqualTo("updated description");
        assertThat(result.getPriority()).isEqualTo(1);
        assertThat(result.getStatus()).isEqualTo(Status.COMPLETE);
    }

    @Test
    public void createInvalidTodo() throws Exception {
        HttpPost post = new HttpPost(local("/todo"));

        post.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

        // missing summary
        JSONObject obj = new JSONObject();
        obj.put("description", "test description");

        post.setEntity(new StringEntity(obj.toJSONString()));

        HttpResponse response = httpClient.execute(post);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

}
