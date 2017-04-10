package uk.co.committedcoding;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

/**
 * Created by Simon Casey on 07/04/2017.
 */
public class TodoApplicationConfiguration extends Configuration {

    @NotNull
    private String dbFilePath;

    @JsonProperty
    public String getDbFilePath() {
        return dbFilePath;
    }

    @JsonProperty
    public void setDbFilePath(String dbFilePath) {
        this.dbFilePath = dbFilePath;
    }
}
