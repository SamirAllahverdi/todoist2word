package org.example.client;

import org.example.config.JsonBodyHandler;
import org.example.config.TodoistConfig;
import org.example.model.Comment;
import org.example.model.Project;
import org.example.model.Section;
import org.example.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class TodoistAPI {
    private static final Logger log = LoggerFactory.getLogger(TodoistAPI.class);

    private final TodoistConfig config;
    private final HttpClient client;

    public TodoistAPI(TodoistConfig config) {
        this.config = config;
        this.client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
    }

    public List<Project> getProjects() {
        var request = HttpRequest.newBuilder(
                        URI.create(config.getApiUrl() + "/projects"))
                .header("Authorization", "Bearer " + config.getApiToken())
                .build();

        try {
            HttpResponse<Project[]> response = client.send(request, new JsonBodyHandler<>(Project[].class));
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch projects statusCode = " + response.statusCode());
            }
            return Arrays.asList(response.body());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch projects", e);
        }
    }

    public List<Section> getSections(String projectId) {
        var request = HttpRequest.newBuilder(
                        URI.create(config.getApiUrl() + "/sections?project_id=" + projectId))
                .header("Authorization", "Bearer " + config.getApiToken())
                .build();

        try {
            HttpResponse<Section[]> response = client.send(request, new JsonBodyHandler<>(Section[].class));
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch sections statusCode = " + response.statusCode());
            }
            return Arrays.asList(response.body());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch sections", e);
        }
    }


    public List<Task> getTasks(String sectionId) {
        var request = HttpRequest.newBuilder(
                        URI.create(config.getApiUrl() + "/tasks?section_id=" + sectionId))
                .header("Authorization", "Bearer " + config.getApiToken())
                .build();

        try {
            HttpResponse<Task[]> response = client.send(request, new JsonBodyHandler<>(Task[].class));
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch tasks statusCode = " + response.statusCode());
            }
            return Arrays.asList(response.body());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch tasks", e);
        }
    }

    public List<Comment> getComments(String taskId) {
        var request = HttpRequest.newBuilder(
                        URI.create(config.getApiUrl() + "/comments?task_id=" + taskId))
                .header("Authorization", "Bearer " + config.getApiToken())
                .build();

        try {
            HttpResponse<Comment[]> response = client.send(request, new JsonBodyHandler<>(Comment[].class));
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch comments statusCode = " + response.statusCode());
            }
            return Arrays.asList(response.body());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch comments", e);
        }
    }

    public InputStream getAttachment(String fileUrl, String fileType) {

        var request = HttpRequest.newBuilder(URI.create(fileUrl))
                .header("Authorization", "Bearer " + config.getApiToken())
                .header("Content-Type", fileType)
                .GET()
                .build();

        try {
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch attachment statusCode = " + response.statusCode());
            }
            return new ByteArrayInputStream(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to fetch attachment url = {} ", fileUrl, e);
        }

        return null;
    }

}
