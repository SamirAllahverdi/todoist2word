package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectResponse {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("is_inbox_project")
    private boolean isInboxProject;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ProjectResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isInboxProject=" + isInboxProject +
                '}';
    }
}
