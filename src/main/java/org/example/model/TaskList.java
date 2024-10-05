package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class TaskList {

    private List<TaskResponse> taskResponse;


    public TaskList() {
        this.taskResponse = new ArrayList<>();
    }

    public List<TaskResponse> getProjectResponse() {
        return taskResponse;
    }

    public void setProjectResponse(List<TaskResponse> taskResponse) {
        this.taskResponse = taskResponse;
    }
}
