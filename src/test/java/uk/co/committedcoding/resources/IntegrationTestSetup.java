package uk.co.committedcoding.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Configuration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import ru.vyarus.dropwizard.guice.injector.lookup.InjectorLookup;
import uk.co.committedcoding.db.TodoRepository;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Simon Casey on 12/04/2017.
 */
public abstract class IntegrationTestSetup<T extends Configuration> {

    final HttpClient httpClient;

    TodoRepository todoRepository;

    ObjectMapper objectMapper;

    IntegrationTestSetup() {
        this.httpClient = HttpClientBuilder.create().build();
        this.todoRepository = getApplicationBean(TodoRepository.class);
        this.objectMapper = getRule().getObjectMapper();
    }

    <T> T getApplicationBean(Class<T> clazz) {
        if (InjectorLookup.getInjector(getRule().getApplication()).isPresent()) {
            T instance = InjectorLookup.getInjector(getRule().getApplication()).get().getInstance(clazz);
            if (instance == null) {
                throw new IllegalArgumentException("No instance of " + clazz.getCanonicalName() + " found,");
            }
            return instance;
        }
        throw new IllegalArgumentException("No Guice injector found");
    }

    <T> T readContent(HttpResponse response, Class<T> clazz) throws Exception {
        return objectMapper.readValue(IOUtils.toString(response.getEntity().getContent()), clazz);
    }

    int status(HttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    URI local(String path) throws URISyntaxException {
        return new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(getRule().getLocalPort())
                .setPath("/api" + path)
                .build();
    }


    abstract DropwizardAppRule<T> getRule();
}
