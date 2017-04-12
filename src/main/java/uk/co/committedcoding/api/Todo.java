package uk.co.committedcoding.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Simon Casey on 07/04/2017.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Todo implements Serializable {

    @NotNull
    @Builder.Default
    private UUID id = UUID.randomUUID();
    @NotNull
    private UUID listId;
    @NotNull
    private String summary;
    private String description;
    @NotNull
    @Builder.Default
    private Integer priority = 1;
    @NotNull
    @Builder.Default
    private Status status = Status.INCOMPLETE;

}