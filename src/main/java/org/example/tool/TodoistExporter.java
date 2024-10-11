package org.example.tool;

import org.example.client.TodoistAPI;
import org.example.config.TodoistConfig;
import org.example.model.Comment;
import org.example.model.Project;
import org.example.model.Section;
import org.example.model.Task;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TodoistExporter {

    private  final TodoistAPI api;
    private  final TodoistConfig config;

    public TodoistExporter(TodoistConfig config) {
        this.api = new TodoistAPI(config);
        this.config = config;
    }

    public void start() {

        List<Project> projects = api.getProjects()
                .stream()
                .filter(p -> config.contains(p.getName()))
                .collect(Collectors.toList());

        for (Project project : projects) {
            WordBuilder wordBuilder = new WordBuilder(config.getWordPath(), config.getOutputPath(), project.getName(), api);
            List<Section> sections = api.getSections( project.getId())
                    .stream().sorted(Comparator.comparingInt(Section::getOrder))
                    .collect(Collectors.toList());

            for (Section section : sections) {
                List<Task> tasks = api.getTasks( section.getId()).stream()
                        .sorted(Comparator.comparingInt(Task::getOrder)).collect(Collectors.toList());
                wordBuilder.export(section);
                for (Task task : tasks) {
                    List<Comment> comments = api.getComments( task.getId());
                    wordBuilder.export(task, comments);
                }
            }
            wordBuilder.build();
        }
    }


}
