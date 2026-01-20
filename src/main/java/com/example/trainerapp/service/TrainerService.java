package com.example.trainerapp.service;

import com.example.trainerapp.entity.Trainer;
import com.example.trainerapp.entity.Subject;
import com.example.trainerapp.repository.TrainerRepository;
import com.example.trainerapp.repository.TrainerSubjectRepository;
import com.example.trainerapp.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerSubjectRepository trainerSubjectRepository;
    private final SubjectRepository subjectRepository;

    public TrainerService(
            TrainerRepository trainerRepository,
            TrainerSubjectRepository trainerSubjectRepository,
            SubjectRepository subjectRepository
    ) {
        this.trainerRepository = trainerRepository;
        this.trainerSubjectRepository = trainerSubjectRepository;
        this.subjectRepository = subjectRepository;
    }

    public Trainer addTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public Optional<Trainer> getTrainerById(Long id) {
        return trainerRepository.findById(id);
    }

    public void deleteTrainer(Long id) {
        trainerRepository.deleteById(id);
    }

    public List<Trainer> getTrainersBySubject(String subjectName) {
        return trainerRepository.findTrainersBySubjectName(subjectName);
    }

    public List<Subject> getSubjectsByTrainer(Long empId) {
        return trainerSubjectRepository.findByEmpId(empId)
                .stream()
                .map(ts -> subjectRepository.findById(ts.getSubjectId()).orElse(null))
                .filter(s -> s != null)
                .toList();
    }
}
