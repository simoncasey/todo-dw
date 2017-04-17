package uk.co.committedcoding.api;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Created by Simon Casey on 10/04/2017.
 */
@Getter
public enum Status {
    DELETED("Deleted", -1),
    INCOMPLETE("Incomplete", 0),
    COMPLETE("Complete", 1);

    private final String name;
    private final int value;

    Status(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}