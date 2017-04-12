package uk.co.committedcoding.resources;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.committedcoding.api.Todo;
import uk.co.committedcoding.db.TodoRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Simon Casey on 07/04/2017.
 */
@Path("/todo")
@Produces(MediaType.APPLICATION_JSON)
public class TodoResource {

    @Inject
    private Provider<UriInfo> uriInfo;

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
    @Timed
    @Path("/list/{listId}")
    public List<Todo> getTodosByListId(@PathParam("listId") UUID listId) {
        return todoRepository.getByListId(listId);
    }

    @GET
    @Path("/{id}")
    @Timed
    public Optional<Todo> getTodo(@PathParam("id") UUID id) {
        return todoRepository.get(id);
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTodo(@NotNull @Valid NewTodo todo) {
        Todo createdTodo = todoRepository.put(todo.build());
        UriBuilder ub = uriInfo.get().getAbsolutePathBuilder();
        URI todoUri = ub.
                path(createdTodo.getId().toString()).
                build();

        return Response.created(todoUri)
                .entity(createdTodo)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Todo updateTodo(@PathParam("id") UUID id, Todo todo) {
        if (!todo.getId().equals(id)) {
            throw new BadRequestException("Todo Id does not match supplied Id");
        } else {
            if (todoRepository.get(id).isPresent()) {
                return todoRepository.put(todo);
            }
            throw new NotFoundException();
        }
    }

    @DELETE
    @Path("/{id}")
    @Timed
    public Response deleteTodo(@PathParam("id") UUID id) {
        if (todoRepository.get(id).isPresent()) {
            todoRepository.delete(id);
            return Response.noContent().build();
        }
        throw new NotFoundException();
    }


}
