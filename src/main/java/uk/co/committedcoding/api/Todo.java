package uk.co.committedcoding.api;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Simon Casey on 07/04/2017.
 */
@Data
public class Todo {

    @NotNull
    private String summary;
    private String description;

    public Todo(String summary) {
        this.summary = summary;
    }
}
