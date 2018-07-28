package uk.co.committedcoding.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import javaslang.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.committedcoding.api.Todo;
import uk.co.committedcoding.db.TodoRepository;
import uk.co.committedcoding.service.TodoService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by Simon Casey on 07/04/2017.
 */
@Path("/todo")
@Produces(MediaType.APPLICATION_JSON)
public class TodoResource {

    @Inject
    private Provider<UriInfo> uriInfo;
    @Inject
    private TodoRepository todoRepository;
    @Inject
    private TodoService todoService;

    final Logger logger = LoggerFactory.getLogger(TodoResource.class);

    @GET
    @Timed
    @UnitOfWork
    public List<Todo> getTodos() {
        return todoRepository.getAll();
    }

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Todo getTodo(@PathParam("id") Long id) {
        return todoRepository.getById(id).getOrElseThrow(NotFoundException::new);
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response createTodo(@NotNull @Valid NewTodo todo) {
        Todo createdTodo = todoService.persist(todo.build());
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
    @UnitOfWork
    public Todo updateTodo(@PathParam("id") Long id, Todo todo) {
        if (!todo.getId().equals(id)) {
            throw new BadRequestException("Todo Id does not match supplied Id");
        } else {
            return todoService.persist(todo);
        }
    }

    @DELETE
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Response deleteTodo(@PathParam("id") Long id) {
        todoService.delete(id);
        return Response.noContent().build();
    }


}
