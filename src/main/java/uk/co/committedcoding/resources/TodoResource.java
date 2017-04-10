package uk.co.committedcoding.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.committedcoding.api.Todo;
import uk.co.committedcoding.db.TodoRepository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Simon Casey on 07/04/2017.
 */
@Path("/todo")
@Produces(MediaType.APPLICATION_JSON)
public class TodoResource {

    final Logger logger = LoggerFactory.getLogger(TodoResource.class);

    private TodoRepository todoRepository;

    @Inject
    public TodoResource(TodoRepository todoRepository) {
        logger.info("Creating a new TodoResource!");
        this.todoRepository = todoRepository;
    }

    @GET
    @Timed
    public List<Todo> getTodos() {

        return todoRepository.getAll();
    }

    @GET
    @Path("/{id}")
    @Timed
    public Optional<Todo> getTodo(@PathParam("id") UUID id) {
        return Optional.empty();
    }

    @POST
    @Timed
    public Todo createTodo(Todo todo) {
        throw new NotImplementedException("No implemented");
    }

    @PUT
    @Path("/{id}")
    @Timed
    public Todo updateTodo(@PathParam("id") UUID id, Todo todo) {
        throw new NotImplementedException("No implemented");
    }

    @DELETE
    @Path("/{id}")
    @Timed
    public Response deleteTodo(@PathParam("id") UUID id) {
        throw new NotImplementedException("No implemented");
    }


}
