package uk.co.committedcoding.views;

import io.dropwizard.views.View;
import lombok.Getter;
import uk.co.committedcoding.api.Todo;

import java.util.List;

/**
 * Created by Simon Casey on 12/04/2017.
 */
@Getter
public class HomeView extends View {

    private final List<Todo> todos;

    public HomeView(List<Todo> todos) {
        super("home.mustache");
        this.todos = todos;
    }
}
