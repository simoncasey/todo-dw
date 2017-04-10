package uk.co.committedcoding.resources;

import lombok.Data;
import uk.co.committedcoding.api.Status;
import uk.co.committedcoding.api.Todo;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Simon Casey on 07/04/2017.
 */
@Data
public class NewTodo {

    private Optional<UUID> listId;
    @NotNull
    private String summary;
    private String description;

    private NewTodo() {}

    public Todo build() {
        return Todo.builder()
                .id(UUID.randomUUID())
                .summary(summary)
                .description(description)
                .listId(listId.orElse(UUID.randomUUID()))
                .priority(1)
                .status(Status.INCOMPLETE)
                .build();
    }
}