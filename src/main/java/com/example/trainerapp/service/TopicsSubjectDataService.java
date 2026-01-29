package com.example.trainerapp.service;

import com.example.trainerapp.entity.TopicsSubjectData;
import com.example.trainerapp.repository.TopicsSubjectDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicsSubjectDataService {

    private final TopicsSubjectDataRepository topicsSubjectDataRepository;

    public TopicsSubjectDataService(TopicsSubjectDataRepository topicsSubjectDataRepository) {
        this.topicsSubjectDataRepository = topicsSubjectDataRepository;
    }

    public TopicsSubjectData assignTopicToTrainer(TopicsSubjectData topicsSubjectData) {
        return topicsSubjectDataRepository.save(topicsSubjectData);
    }

    public List<TopicsSubjectData> getAssignmentsBySubject(Long subjectId) {
        return topicsSubjectDataRepository.findBySubjectId(subjectId);
    }

    public List<TopicsSubjectData> getAssignmentsByTrainer(Long trainerId) {
        return topicsSubjectDataRepository.findByTrainerId(trainerId);
    }

    public void unassignTopicFromTrainer(Long subjectId, Long trainerId, Long topicId) {
        List<TopicsSubjectData> assignments = topicsSubjectDataRepository.findByTrainerIdAndSubjectId(trainerId, subjectId);
        assignments.stream()
                .filter(assignment -> assignment.getTopicId().equals(topicId))
                .forEach(topicsSubjectDataRepository::delete);
    }
}
