package org.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "todoist")
public class TodoistConfig {
    private String authUrl;
    private String apiUrl;
    private String scope;
    private String clientId;
    private List<String> projects;
    private String wordName;
    private String outputPath;


    public boolean contains(String name) {
        if (projects == null || projects.isEmpty())
            return true;

        return projects.contains(name);
    }

    public String getWordPath() {
        return Path.of(outputPath, wordName).toString();
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "TodoistConfig{" +
                "authUrl='" + authUrl + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
