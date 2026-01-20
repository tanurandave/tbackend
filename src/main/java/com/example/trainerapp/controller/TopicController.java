package com.example.trainerapp.controller;

import com.example.trainerapp.entity.Topic;
import com.example.trainerapp.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@CrossOrigin("*")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        System.out.println("TopicController constructor called");
        this.topicService = topicService;
    }

@PostMapping
public Topic addTopic(@Valid @RequestBody Topic topic) {
    return topicService.addTopic(topic);
}

    @GetMapping
    public List<Topic> getAllTopics() {
        System.out.println("Fetching all topics");
        return topicService.getAllTopics();
    }
}
