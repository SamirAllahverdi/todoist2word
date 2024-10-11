package org.example.config;

import org.apache.poi.util.StringUtil;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TodoistConfig {
    private final String apiUrl;
    private final List<String> projects;
    private final String wordName;
    private final String outputPath;
    private final String apiToken;

    public TodoistConfig(Properties props) {
        this.apiUrl = props.getProperty("todoist.api.url");
        this.apiToken = props.getProperty("todoist.api.token");
        if (StringUtil.isBlank(apiToken))
            throw new IllegalArgumentException("Token not defined [todoist.api.token]");
        this.projects = Arrays.asList(props.getProperty("todoist.projects").split(","));
        this.wordName = props.getProperty("todoist.word-name");
        this.outputPath = props.getProperty("todoist.output-path");
    }

    public boolean contains(String name) {
        if (projects == null || projects.isEmpty()) {
            return true;
        }
        return projects.contains(name);
    }

    public String getWordPath() {
        return Path.of(outputPath, wordName).toString();
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public List<String> getProjects() {
        return projects;
    }

    public String getWordName() {
        return wordName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getApiToken() {
        return apiToken;
    }
}
