package org.example.controller;

import org.example.config.TodoistConfig;
import org.example.service.TodoistService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
public class TodoistController {

    private final TodoistService service;
    private final TodoistConfig config;

    public TodoistController(TodoistService service, TodoistConfig config) {
        this.service = service;
        this.config = config;
    }

    @GetMapping
    public RedirectView redirect() {
        String state = UUID.randomUUID().toString().replace("-", "");
        return new RedirectView(
                UriComponentsBuilder.fromHttpUrl(config.getAuthUrl())
                        .queryParam("client_id", config.getClientId())
                        .queryParam("scope", config.getScope())
                        .queryParam("state", state).toUriString()
        );
    }

    @GetMapping("/start")
    public String start(@RequestParam String code) {
        service.start(code);
        return "Process finished!";
    }
}
