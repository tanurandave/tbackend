package com.example.trainerapp.controller;

import com.example.trainerapp.entity.TopicsSubjectData;
import com.example.trainerapp.service.TopicsSubjectDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics-subject-data")
@CrossOrigin("*")
public class TopicsSubjectDataController {

    private final TopicsSubjectDataService topicsSubjectDataService;

    public TopicsSubjectDataController(TopicsSubjectDataService topicsSubjectDataService) {
        this.topicsSubjectDataService = topicsSubjectDataService;
    }

    @PostMapping("/assign")
    public ResponseEntity<TopicsSubjectData> assignTopicToTrainer(@RequestBody TopicsSubjectData topicsSubjectData) {
        try {
            TopicsSubjectData saved = topicsSubjectDataService.assignTopicToTrainer(topicsSubjectData);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<TopicsSubjectData>> getAssignmentsBySubject(@PathVariable Long subjectId) {
        List<TopicsSubjectData> assignments = topicsSubjectDataService.getAssignmentsBySubject(subjectId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<TopicsSubjectData>> getAssignmentsByTrainer(@PathVariable Long trainerId) {
        List<TopicsSubjectData> assignments = topicsSubjectDataService.getAssignmentsByTrainer(trainerId);
        return ResponseEntity.ok(assignments);
    }

    @DeleteMapping("/unassign")
    public ResponseEntity<Void> unassignTopicFromTrainer(@RequestParam Long subjectId, @RequestParam Long trainerId, @RequestParam Long topicId) {
        topicsSubjectDataService.unassignTopicFromTrainer(subjectId, trainerId, topicId);
        return ResponseEntity.noContent().build();
    }
}
