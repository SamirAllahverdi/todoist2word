package org.example.client;

import org.example.config.TodoistConfig;
import org.example.model.CommentResponse;
import org.example.model.ProjectResponse;
import org.example.model.SectionResponse;
import org.example.model.TaskResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Component
public class TodoistAPI {

    private final RestTemplate restTemplate;
    private final TodoistConfig config;

    public TodoistAPI(RestTemplate restTemplate, TodoistConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public List<ProjectResponse> getProjects(String code) {

        ResponseEntity<List<ProjectResponse>> exchange = restTemplate.exchange(
                config.getApiUrl() + "/projects", HttpMethod.GET, buildBasicHttpEntity(code), new ParameterizedTypeReference<>() {
                });
        if (exchange.getStatusCode() != HttpStatus.OK)
            throw new RuntimeException("Failed to fetch projects");

        return exchange.getBody();
    }

    public List<SectionResponse> getSections(String code, String projectId) {
        ResponseEntity<List<SectionResponse>> exchange = restTemplate.exchange(
                config.getApiUrl() + "/sections?project_id=" + projectId, HttpMethod.GET, buildBasicHttpEntity(code), new ParameterizedTypeReference<>() {
                });
        if (exchange.getStatusCode() != HttpStatus.OK)
            throw new RuntimeException("Failed to fetch sections");

        return exchange.getBody();
    }

    public List<TaskResponse> getTasks(String code, String sectionId) {
        ResponseEntity<List<TaskResponse>> exchange = restTemplate.exchange(
                config.getApiUrl() + "/tasks?section_id=" + sectionId, HttpMethod.GET,
                buildBasicHttpEntity(code), new ParameterizedTypeReference<>() {});

        if (exchange.getStatusCode() != HttpStatus.OK)
            throw new RuntimeException("Failed to fetch tasks");

        return exchange.getBody();
    }

    public List<CommentResponse> getComments(String code, String taskId) {

        ResponseEntity<List<CommentResponse>> exchange = restTemplate.exchange(
                config.getApiUrl() + "/comments?task_id=" + taskId, HttpMethod.GET, buildBasicHttpEntity(code), new ParameterizedTypeReference<>() {
                });
        if (exchange.getStatusCode() != HttpStatus.OK)
            throw new RuntimeException("Failed to fetch comments");

        return exchange.getBody();
    }

    private static HttpEntity<Object> buildBasicHttpEntity(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + code);

        return new HttpEntity<>(headers);
    }

    public InputStream getAttachment(String code, String fileUrl, String fileType) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + code);
        headers.add(HttpHeaders.CONTENT_TYPE, fileType);

        ResponseEntity<byte[]> exchange = restTemplate.exchange(fileUrl, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);

        if (exchange.getStatusCode() != HttpStatus.OK)
            throw new RuntimeException("Failed to fetch attachment");


        return new ByteArrayInputStream(exchange.getBody());
    }
}
