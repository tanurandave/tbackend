package com.example.trainerapp.service;

import com.example.trainerapp.entity.*;
import com.example.trainerapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final SubjectTopicRepository subjectTopicRepository;
    private final TrainerSubjectRepository trainerSubjectRepository;
    private final TrainerRepository trainerRepository;
    private final TopicsSubjectDataRepository topicsSubjectDataRepository;

    public SubjectService(
            SubjectRepository subjectRepository,
            TopicRepository topicRepository,
            SubjectTopicRepository subjectTopicRepository,
            TrainerSubjectRepository trainerSubjectRepository,
            TrainerRepository trainerRepository,
            TopicsSubjectDataRepository topicsSubjectDataRepository
    ) {
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.subjectTopicRepository = subjectTopicRepository;
        this.trainerSubjectRepository = trainerSubjectRepository;
        this.trainerRepository = trainerRepository;
        this.topicsSubjectDataRepository = topicsSubjectDataRepository;
    }

    public Subject addSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public List<Topic> getTopicsForSubject(Long subjectId) {
        return subjectTopicRepository.findBySubjectId(subjectId)
                .stream()
                .map(st -> topicRepository.findById(st.getTopicId()).orElse(null))
                .filter(t -> t != null)
                .collect(Collectors.toList());
    }

    // ⭐ CORE LOGIC
    public void addTopicsToSubject(Long subjectId, SubjectTopicRequest request) {

        // Add existing topics
        if (request.getTopicIds() != null) {
            for (Long topicId : request.getTopicIds()) {
                saveMapping(subjectId, topicId);
            }
        }

        // Add new topics
        if (request.getTopics() != null) {
            for (Topic topic : request.getTopics()) {
                Topic saved = topicRepository.save(topic);
                saveMapping(subjectId, saved.getTopicId());
            }
        }
    }

    private void saveMapping(Long subjectId, Long topicId) {
        boolean exists = subjectTopicRepository
                .existsBySubjectIdAndTopicId(subjectId, topicId);

        if (!exists) {
            SubjectTopic st = new SubjectTopic();
            st.setSubjectId(subjectId);
            st.setTopicId(topicId);
            subjectTopicRepository.save(st);
        }
    }

    public void removeTopicFromSubject(Long subjectId, Long topicId) {
        subjectTopicRepository.deleteBySubjectIdAndTopicId(subjectId, topicId);
    }

    @Transactional
    public void deleteSubject(Long id) {
        // Delete trainer-subject assignments first
        trainerSubjectRepository.deleteBySubjectId(id);

        // Delete topics-subject-data assignments
        topicsSubjectDataRepository.deleteBySubjectId(id);

        // Get topicIds for this subject
        List<Long> topicIds = subjectTopicRepository.findBySubjectId(id).stream()
                .map(SubjectTopic::getTopicId)
                .collect(Collectors.toList());

        // Delete topics that are not associated with other subjects
        for (Long topicId : topicIds) {
            if (subjectTopicRepository.countByTopicIdAndSubjectIdNot(topicId, id) == 0) {
                // Also delete from topics-subject-data if not already done
                topicsSubjectDataRepository.deleteByTopicId(topicId);
                topicRepository.deleteById(topicId);
            }
        }

        // Delete subject-topic mappings
        subjectTopicRepository.deleteBySubjectId(id);

        // Delete the subject
        subjectRepository.deleteById(id);
    }

    public SubjectWithTrainers getSubjectWithTrainers(Long id) {
        Subject subject = subjectRepository.findById(id).orElse(null);
        if (subject == null) {
            return null;
        }

        // Fetch assigned trainers for the subject
        List<TrainerSubject> assignments = trainerSubjectRepository.findBySubjectId(id);
        List<Trainer> trainers = assignments.stream()
                .map(assignment -> trainerRepository.findById(assignment.getEmpId()).orElse(null))
                .filter(trainer -> trainer != null)
                .collect(Collectors.toList());

        return new SubjectWithTrainers(subject, trainers);
    }

    public List<Topic> getAssignedTopicsForTrainerAndSubject(Long trainerId, Long subjectId) {
        return topicsSubjectDataRepository.findByTrainerIdAndSubjectId(trainerId, subjectId)
                .stream()
                .map(tsd -> topicRepository.findById(tsd.getTopicId()).orElse(null))
                .filter(t -> t != null)
                .collect(Collectors.toList());
    }

    public List<TopicWithTrainer> getAssignedTopicsWithTrainersForSubject(Long subjectId) {
        List<TopicsSubjectData> assignments = topicsSubjectDataRepository.findBySubjectId(subjectId);
        List<TopicWithTrainer> result = new ArrayList<>();
        for (TopicsSubjectData tsd : assignments) {
            Topic topic = topicRepository.findById(tsd.getTopicId()).orElse(null);
            Trainer trainer = trainerRepository.findById(tsd.getTrainerId()).orElse(null);
            if (topic != null && trainer != null) {
                result.add(new TopicWithTrainer(topic.getTopicId(), topic.getTopicName(), topic.getDescription(), trainer.getEmpId(), trainer.getName()));
            }
        }
        return result;
    }
}
