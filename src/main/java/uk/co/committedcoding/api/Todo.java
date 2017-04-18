package uk.co.committedcoding.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Simon Casey on 07/04/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "Todo")
@NamedQueries({
    @NamedQuery(
        name = "uk.co.committedcoding.api.Todo.findAll",
        query = "FROM Todo t ORDER BY t.priority DESC"
    )
})
public class Todo implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
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