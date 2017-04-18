package uk.co.committedcoding.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.committedcoding.api.Todo;
import uk.co.committedcoding.db.TodoRepository;
import uk.co.committedcoding.views.HomeView;

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
import java.util.Optional;

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

    final Logger logger = LoggerFactory.getLogger(TodoResource.class);

    @GET
    @Timed
    @UnitOfWork
    public List<Todo> getTodos() {
        return todoRepository.getAll();
    }

    @GET
    @Timed
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public HomeView getHomeView() {
        return new HomeView(todoRepository.getAll());
    }

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Optional<Todo> getTodo(@PathParam("id") Long id) {
        return todoRepository.getById(id);
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response createTodo(@NotNull @Valid NewTodo todo) {
        Todo createdTodo = todoRepository.create(todo.build());
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
            return todoRepository.update(todo).orElseThrow(NotFoundException::new);
        }
    }

    @DELETE
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Response deleteTodo(@PathParam("id") Long id) {
        todoRepository.delete(id);
        return Response.noContent().build();
    }


}
