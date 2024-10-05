package org.example.service;

import org.example.client.TodoistAPI;
import org.example.config.TodoistConfig;
import org.example.model.CommentResponse;
import org.example.model.ProjectResponse;
import org.example.model.SectionResponse;
import org.example.model.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoistService {

    private final TodoistAPI api;
    private final TodoistConfig config;

    public TodoistService(TodoistAPI api, TodoistConfig config) {
        this.api = api;
        this.config = config;
    }

    public void start(String code) {

        List<ProjectResponse> projects = api.getProjects(code)
                .stream()
                .filter(p -> config.contains(p.getName()))
                .collect(Collectors.toList());

        for (ProjectResponse project : projects) {
            WordBuilder wordBuilder = new WordBuilder(config.getWordPath(), config.getOutputPath(), code, project.getName(), api);
            List<SectionResponse> sections = api.getSections(code, project.getId())
                    .stream().sorted(Comparator.comparingInt(SectionResponse::getOrder))
                    .collect(Collectors.toList());

            for (SectionResponse section : sections) {
                List<TaskResponse> tasks = api.getTasks(code, section.getId()).stream()
                        .sorted(Comparator.comparingInt(TaskResponse::getOrder)).collect(Collectors.toList());
                wordBuilder.export(section);
                for (TaskResponse task : tasks) {
                    List<CommentResponse> comments = api.getComments(code, task.getId());
                    wordBuilder.export(task, comments);
                }
            }
            wordBuilder.build();
        }
    }


}
