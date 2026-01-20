package com.example.trainerapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.trainerapp.entity.TrainerSubject;
import com.example.trainerapp.repository.TrainerSubjectRepository;
import com.example.trainerapp.repository.TrainerRepository;
import com.example.trainerapp.repository.SubjectRepository;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerSubjectService {

    @Autowired
    private TrainerSubjectRepository trainerSubjectRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    /**
     * Get all trainer-subject assignments
     */
    public List<TrainerSubject> getAllAssignments() {
        return trainerSubjectRepository.findAll();
    }

    /**
     * Get assignment by ID
     */
    public Optional<TrainerSubject> getAssignmentById(Long id) {
        return trainerSubjectRepository.findById(id);
    }

    /**
     * Get all assignments for a specific trainer
     */
    public List<TrainerSubject> getAssignmentsByTrainer(Long empId) {
        return trainerSubjectRepository.findByEmpId(empId);
    }

    /**
     * Get all assignments for a specific subject
     */
    public List<TrainerSubject> getAssignmentsBySubject(Long subjectId) {
        return trainerSubjectRepository.findBySubjectId(subjectId);
    }

    /**
     * Assign a trainer to a subject
     * Prevents duplicate assignments using database constraint
     */
    public TrainerSubject assignTrainerToSubject(TrainerSubject trainerSubject)
            throws Exception {

        // Validate input
        if (trainerSubject.getEmpId() == null || trainerSubject.getSubjectId() == null) {
            throw new Exception("Trainer ID and Subject ID are required!");
        }

        // Check if assignment already exists
        if (trainerSubjectRepository.existsByEmpIdAndSubjectId(
                trainerSubject.getEmpId(), trainerSubject.getSubjectId())) {
            throw new Exception("This trainer is already assigned to this subject!");
        }

        // Populate trainer name and subject name
        var trainer = trainerRepository.findById(trainerSubject.getEmpId())
                .orElseThrow(() -> new Exception("Trainer not found!"));
        var subject = subjectRepository.findById(trainerSubject.getSubjectId())
                .orElseThrow(() -> new Exception("Subject not found!"));

        trainerSubject.setTrainerName(trainer.getName());
        trainerSubject.setSubjectName(subject.getSubjectName());

        try {
            return trainerSubjectRepository.save(trainerSubject);
        } catch (Exception e) {
            // Handle duplicate key constraint violation
            if (e.getMessage() != null &&
                (e.getMessage().contains("Duplicate") || e.getMessage().contains("UNIQUE"))) {
                throw new Exception("This trainer is already assigned to this subject!");
            }
            throw new Exception("Error assigning trainer: " + e.getMessage());
        }
    }

    /**
     * Delete assignment by trainer and subject
     * Uses optimized delete query
     */
    public boolean deleteAssignment(Long empId, Long subjectId) {
        try {
            int deletedCount = trainerSubjectRepository.deleteByEmpIdAndSubjectId(empId, subjectId);
            return deletedCount > 0;
        } catch (Exception e) {
            System.out.println("Error deleting assignment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete assignment by ID
     */
    public boolean deleteAssignmentById(Long id) {
        try {
            if (trainerSubjectRepository.existsById(id)) {
                trainerSubjectRepository.deleteById(id);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error deleting assignment by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if a trainer is assigned to a subject
     */
    public boolean isAssigned(Long empId, Long subjectId) {
        return trainerSubjectRepository.existsByEmpIdAndSubjectId(empId, subjectId);
    }

    /**
     * Get count of assignments for a trainer
     */
    public long getTrainerAssignmentCount(Long empId) {
        return getAssignmentsByTrainer(empId).size();
    }

    /**
     * Get count of trainers assigned to a subject
     */
    public long getSubjectTrainerCount(Long subjectId) {
        return getAssignmentsBySubject(subjectId).size();
    }

    /**
     * Update assignment (replace old subject with new subject for a trainer)
     */
    public TrainerSubject updateAssignment(Long empId, Long oldSubjectId, Long newSubjectId) 
            throws Exception {
        
        // Delete old assignment
        if (!deleteAssignment(empId, oldSubjectId)) {
            throw new Exception("Old assignment not found!");
        }

        // Create new assignment
        TrainerSubject newAssignment = new TrainerSubject();
        newAssignment.setEmpId(empId);
        newAssignment.setSubjectId(newSubjectId);

        return assignTrainerToSubject(newAssignment);
    }

    /**
     * Clean up duplicate assignments
     * Keeps the first one, deletes the rest
     */
    public int cleanupDuplicates() {
        try {
            int deletedCount = trainerSubjectRepository.deleteDuplicates();
            System.out.println("Deleted " + deletedCount + " duplicate assignments");
            return deletedCount;
        } catch (Exception e) {
            System.out.println("Error cleaning duplicates: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}
