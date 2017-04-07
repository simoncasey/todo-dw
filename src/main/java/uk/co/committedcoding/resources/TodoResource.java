package uk.co.committedcoding.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import uk.co.committedcoding.api.Todo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Simon Casey on 07/04/2017.
 */
@Path("/todo")
@Produces(MediaType.APPLICATION_JSON)
public class TodoResource {

    @GET
    @Timed
    public List<Todo> getTodos() {
        Todo test1 = new Todo("test 1");
        Todo test2 = new Todo("test 2");
        Todo test3 = new Todo("test 3");
        return Lists.newArrayList(test1, test2, test3);
    }

}
