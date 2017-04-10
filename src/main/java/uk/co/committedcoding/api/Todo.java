package uk.co.committedcoding.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Simon Casey on 07/04/2017.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Todo {

    @NotNull
    private UUID id = UUID.randomUUID();
    @NotNull
    private UUID listId = UUID.randomUUID();
    @NotNull
    private String summary;
    private String description;
    @NotNull
    private Integer priority = 1;
    @NotNull
    private Status status = Status.INCOMPLETE;

    public Todo(String summary) {
        this.summary = summary;
    }

    public Todo(String summary, String description) {
        this.summary = summary;
        this.description = description;
    }

    public Todo(UUID listId, String summary) {
        this.listId = listId;
        this.summary = summary;
    }

    public Todo(UUID listId, String summary, String description) {
        this.listId = listId;
        this.summary = summary;
        this.description = description;
    }
}