package com.example.trainerapp.service;

import com.example.trainerapp.entity.Trainer;
import com.example.trainerapp.entity.Subject;
import com.example.trainerapp.entity.TrainerWithTopics;
import com.example.trainerapp.repository.TrainerRepository;
import com.example.trainerapp.repository.TrainerSubjectRepository;
import com.example.trainerapp.repository.SubjectRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Trainer> getTrainersBySubjectId(Long subjectId) {
        return trainerRepository.findTrainersBySubjectId(subjectId);
    }

    public List<TrainerWithTopics> getTrainersWithTopicsBySubjectId(Long subjectId) {
        List<Object[]> results = trainerRepository.findTrainersWithTopicsBySubjectId(subjectId);
        List<TrainerWithTopics> trainersWithTopics = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (Object[] row : results) {
            Long empId = (Long) row[0];
            String name = (String) row[1];
            String email = (String) row[2];
            int experience = (Integer) row[3];
            String address = (String) row[4];
            String format = (String) row[5];
            String mobileNumber = (String) row[6];
            String topicNamesJson = (String) row[7];

            List<String> topicNames = new ArrayList<>();
            if (topicNamesJson != null && !topicNamesJson.equals("[null]")) {
                try {
                    topicNames = objectMapper.readValue(topicNamesJson, new TypeReference<List<String>>() {});
                } catch (Exception e) {
                    // Handle parsing error, perhaps log it
                }
            }

            TrainerWithTopics trainerWithTopics = new TrainerWithTopics(empId, name, email, experience, address, format, mobileNumber, topicNames);
            trainersWithTopics.add(trainerWithTopics);
        }

        return trainersWithTopics;
    }
}
