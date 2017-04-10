package uk.co.committedcoding.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.NotImplementedException;
import uk.co.committedcoding.api.Todo;

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

    @GET
    @Timed
    public List<Todo> getTodos() {
        Todo test1 = new Todo("test 1", "a test description for 1");
        Todo test2 = new Todo("test 2");
        Todo test3 = new Todo("test 3");
        return Lists.newArrayList(test1, test2, test3);
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
