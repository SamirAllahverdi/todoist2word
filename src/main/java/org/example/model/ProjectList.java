package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class ProjectList {

    private List<ProjectResponse> projectResponse;


    public ProjectList() {
        this.projectResponse = new ArrayList<>();
    }

    public List<ProjectResponse> getProjectResponse() {
        return projectResponse;
    }

    public void setProjectResponse(List<ProjectResponse> projectResponse) {
        this.projectResponse = projectResponse;
    }
}
