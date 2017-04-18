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
import ru.vyarus.dropwizard.guice.test.GuiceyAppRule;
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
            new DropwizardAppRule<TodoApplicationConfiguration>(TodoApplication.class, ResourceHelpers.resourceFilePath("test-todo-dw.yml"));

    DropwizardAppRule<TodoApplicationConfiguration> getRule() {
        return RULE;
    }

    @Before
    public void beforeEach() {
    }

    @AfterClass
    public static void tearDown() {
    }

    @Test
    public void getValidTodo() throws Exception {

        Todo todo1 = Todo.builder()
                .summary("some summary")
                .build();

        final Long todoId = todoRepository.create(todo1).getId();

        HttpGet get = new HttpGet(local("/todo/" + todoId.toString()));

        HttpResponse response = httpClient.execute(get);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_OK);

        Todo result = readContent(response, Todo.class);

        assertThat(result.getId()).isEqualTo(todoId);
        assertThat(result.getSummary()).isEqualTo("some summary");
    }

    @Test
    public void getInvalidTodo() throws Exception {
        Todo todo1 = Todo.builder()
                .summary("some summary")
                .build();

        todoRepository.create(todo1);

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

        final Long todoId = todoRepository.create(todo1).getId();

        HttpPut put = new HttpPut(local("/todo/" + todoId.toString()));

        put.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

        JSONObject obj = new JSONObject();
        obj.put("id", todoId.toString());
        obj.put("summary", "updated summary");
        obj.put("description", "updated description");
        obj.put("priority", 10);
        obj.put("status", Status.COMPLETE.getName());

        put.setEntity(new StringEntity(obj.toJSONString()));

        HttpResponse response = httpClient.execute(put);

        assertThat(status(response)).isEqualTo(HttpStatus.SC_OK);

        Todo result = readContent(response, Todo.class);

        assertThat(result.getSummary()).isEqualTo("updated summary");
        assertThat(result.getDescription()).isEqualTo("updated description");
        assertThat(result.getPriority()).isEqualTo(10);
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
