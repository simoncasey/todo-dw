package uk.co.committedcoding.resources;

import lombok.Data;
import uk.co.committedcoding.api.Status;
import uk.co.committedcoding.api.Todo;

import javax.validation.constraints.NotNull;

/**
 * Created by Simon Casey on 07/04/2017.
 */
@Data
public class NewTodo {

    @NotNull
    private String summary;
    private String description;

    private NewTodo() {}

    public NewTodo(String summary, String description) {
        this.summary = summary;
        this.description = description;
    }

    public Todo build() {
        return Todo.builder()
                .summary(summary)
                .description(description)
                .priority(1)
                .status(Status.INCOMPLETE)
                .build();
    }
}