package com.example.trainerapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.trainerapp.entity.TrainerSubject;
import com.example.trainerapp.service.TrainerSubjectService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainer-subject")
@CrossOrigin(origins = "*")
public class TrainerSubjectController {

    @Autowired
    private TrainerSubjectService trainerSubjectService;

    // GET all assignments
    @GetMapping
    public ResponseEntity<List<TrainerSubject>> getAllAssignments() {
        try {
            List<TrainerSubject> assignments = trainerSubjectService.getAllAssignments();
            System.out.println("Total assignments: " + assignments.size());
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            System.out.println("Error fetching assignments: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // GET assignment by id
    @GetMapping("/{id}")
    public ResponseEntity<TrainerSubject> getAssignmentById(@PathVariable Long id) {
        Optional<TrainerSubject> assignment = trainerSubjectService.getAssignmentById(id);
        return assignment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET assignments by trainer (empId)
    @GetMapping("/trainer/{empId}")
    public ResponseEntity<List<TrainerSubject>> getAssignmentsByTrainer(@PathVariable Long empId) {
        List<TrainerSubject> assignments = trainerSubjectService.getAssignmentsByTrainer(empId);
        return ResponseEntity.ok(assignments);
    }

    // GET assignments by subject (subjectId)
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<TrainerSubject>> getAssignmentsBySubject(@PathVariable Long subjectId) {
        List<TrainerSubject> assignments = trainerSubjectService.getAssignmentsBySubject(subjectId);
        return ResponseEntity.ok(assignments);
    }

    // ASSIGN trainer to subject
    @PostMapping("/assign")
    public ResponseEntity<?> assignTrainerToSubject(@RequestBody TrainerSubject trainerSubject) {
        try {
            TrainerSubject savedAssignment = trainerSubjectService.assignTrainerToSubject(trainerSubject);
            return ResponseEntity.ok(savedAssignment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE assignment by trainer and subject
    @DeleteMapping("/{empId}/{subjectId}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long empId, @PathVariable Long subjectId) {
        try {
            System.out.println("DELETE request - empId: " + empId + ", subjectId: " + subjectId);
            
            if (empId == null || subjectId == null) {
                return ResponseEntity.badRequest().body("Trainer ID and Subject ID are required!");
            }
            
            boolean deleted = trainerSubjectService.deleteAssignment(empId, subjectId);
            
            if (deleted) {
                System.out.println("Assignment deleted successfully");
                return ResponseEntity.ok("Assignment deleted successfully!");
            }
            
            System.out.println("Assignment not found");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println("Error deleting assignment: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // DELETE assignment by id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAssignmentById(@PathVariable Long id) {
        boolean deleted = trainerSubjectService.deleteAssignmentById(id);
        
        if (deleted) {
            return ResponseEntity.ok("Assignment deleted successfully!");
        }
        return ResponseEntity.notFound().build();
    }

    // CHECK if trainer is assigned to subject
    @GetMapping("/check/{empId}/{subjectId}")
    public ResponseEntity<Boolean> isAssigned(@PathVariable Long empId, @PathVariable Long subjectId) {
        boolean isAssigned = trainerSubjectService.isAssigned(empId, subjectId);
        return ResponseEntity.ok(isAssigned);
    }

    // GET count of assignments for a trainer
    @GetMapping("/count/trainer/{empId}")
    public ResponseEntity<Long> getTrainerAssignmentCount(@PathVariable Long empId) {
        long count = trainerSubjectService.getTrainerAssignmentCount(empId);
        return ResponseEntity.ok(count);
    }

    // GET count of trainers assigned to a subject
    @GetMapping("/count/subject/{subjectId}")
    public ResponseEntity<Long> getSubjectTrainerCount(@PathVariable Long subjectId) {
        long count = trainerSubjectService.getSubjectTrainerCount(subjectId);
        return ResponseEntity.ok(count);
    }

    // CLEANUP duplicate assignments
    @PostMapping("/cleanup-duplicates")
    public ResponseEntity<?> cleanupDuplicates() {
        try {
            int deletedCount = trainerSubjectService.cleanupDuplicates();
            return ResponseEntity.ok("Cleaned up " + deletedCount + " duplicate assignments");
        } catch (Exception e) {
            System.out.println("Error cleaning duplicates: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}

