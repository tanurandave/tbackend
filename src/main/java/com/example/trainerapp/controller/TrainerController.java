package com.example.trainerapp.controller;

import com.example.trainerapp.entity.Trainer;
import com.example.trainerapp.entity.Subject;
import com.example.trainerapp.service.TrainerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainer")
@CrossOrigin("*")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    // ✅ CREATE TRAINER
    @PostMapping
    public ResponseEntity<Trainer> addTrainer(@RequestBody Trainer trainer) {
        return ResponseEntity.ok(trainerService.addTrainer(trainer));
    }

    // ✅ GET ALL TRAINERS
    @GetMapping
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        return ResponseEntity.ok(trainerService.getAllTrainers());
    }

    // ✅ GET TRAINER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Trainer> getTrainerById(@PathVariable Long id) {
        return trainerService.getTrainerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE TRAINER
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ GET TRAINERS BY SUBJECT NAME (FIXED PATH)
    @GetMapping("/by-subject/{subjectName}")
    public ResponseEntity<List<Trainer>> getTrainersBySubject(
            @PathVariable String subjectName) {
        return ResponseEntity.ok(
                trainerService.getTrainersBySubject(subjectName)
        );
    }

    // ✅ GET SUBJECTS FOR A TRAINER
    @GetMapping("/{id}/subjects")
    public ResponseEntity<List<Subject>> getSubjectsForTrainer(@PathVariable Long id) {
        return ResponseEntity.ok(trainerService.getSubjectsByTrainer(id));
    }
}
