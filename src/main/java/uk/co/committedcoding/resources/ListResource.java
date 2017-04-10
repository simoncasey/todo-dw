package uk.co.committedcoding.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.NotImplementedException;
import uk.co.committedcoding.api.Todo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Simon Casey on 07/04/2017.
 */
@Path("/list")
@Produces(MediaType.APPLICATION_JSON)
public class ListResource {

    @GET
    @Path("/{listId}")
    @Timed
    public List<Todo> getTodosByListId(@PathParam("listId") UUID listId) {
        return new ArrayList<>();
    }


}
