package uk.co.committedcoding.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.*;
import uk.co.committedcoding.TodoApplication;
import uk.co.committedcoding.TodoApplicationConfiguration;
import uk.co.committedcoding.api.Todo;
import uk.co.committedcoding.db.TodoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Simon Casey on 10/04/2017.
 */
public class TodoResourceTest {


    final HttpClient httpClient = HttpClientBuilder.create().build();

    @ClassRule
    public static final DropwizardAppRule<TodoApplicationConfiguration> RULE =
            new DropwizardAppRule<TodoApplicationConfiguration>(TodoApplication.class, ResourceHelpers.resourceFilePath("test-todo-dw.yml"));

    @BeforeClass
    public static void beforeClass() {
        JerseyGuiceUtils.reset();
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
    public void testHelloWorld() throws Exception {

        TodoRepository todoRepository = RULE.getEnvironment().getApplicationContext().getBean(TodoRepository.class);

        UUID todoId = UUID.randomUUID();
        UUID listId = UUID.randomUUID();
        todoRepository.put(Todo.builder().id(todoId).summary("some summary").listId(listId).build());

//        HttpPost post = new HttpPost("http://localhost:" + RULE.getLocalPort() + "/todo");
//
//        post.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
//
//        NewTodo newTodo = new NewTodo("test101", "some description");
//        String str = new ObjectMapper().writeValueAsString(newTodo);
//
//        post.setEntity(new StringEntity(str));

        HttpGet get = new HttpGet("http://localhost:" + RULE.getLocalPort() + "/todo/" + todoId.toString());

        HttpResponse response = httpClient.execute(get);

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);

        Todo result = new ObjectMapper().readValue(IOUtils.toString(response.getEntity().getContent()), Todo.class);

        System.out.println(result.toString());
        assertThat(result.getId()).isEqualTo(todoId);
        assertThat(result.getListId()).isEqualTo(listId);
        assertThat(result.getSummary()).isEqualTo("some summary");
    }

}
