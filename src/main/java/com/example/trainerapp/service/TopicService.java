package com.example.trainerapp.service;

import com.example.trainerapp.entity.SubjectTopic;
import com.example.trainerapp.entity.Topic;
import com.example.trainerapp.repository.SubjectTopicRepository;
import com.example.trainerapp.repository.TopicRepository;
import com.example.trainerapp.repository.TopicsSubjectDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final SubjectTopicRepository subjectTopicRepository;
    private final TopicsSubjectDataRepository topicsSubjectDataRepository;

    public TopicService(TopicRepository topicRepository, SubjectTopicRepository subjectTopicRepository, TopicsSubjectDataRepository topicsSubjectDataRepository) {
        this.topicRepository = topicRepository;
        this.subjectTopicRepository = subjectTopicRepository;
        this.topicsSubjectDataRepository = topicsSubjectDataRepository;
    }

    public Topic addTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id).orElse(null);
    }

    public List<Topic> getTopicsForSubject(Long subjectId) {
        List<SubjectTopic> subjectTopics = subjectTopicRepository.findBySubjectId(subjectId);
        return subjectTopics.stream()
                .map(st -> topicRepository.findById(st.getTopicId()).orElse(null))
                .filter(topic -> topic != null)
                .collect(Collectors.toList());
    }

    public Topic addTopicToSubject(Long subjectId, Topic topic) {
        Topic savedTopic = topicRepository.save(topic);
        SubjectTopic subjectTopic = new SubjectTopic();
        subjectTopic.setSubjectId(subjectId);
        subjectTopic.setTopicId(savedTopic.getTopicId());
        subjectTopicRepository.save(subjectTopic);
        return savedTopic;
    }

    @Transactional
    public void deleteTopicFromSubject(Long subjectId, Long topicId) {
        subjectTopicRepository.deleteBySubjectIdAndTopicId(subjectId, topicId);
        // Check if the topic is associated with any other subjects
        List<SubjectTopic> remainingAssociations = subjectTopicRepository.findByTopicId(topicId);
        if (remainingAssociations.isEmpty()) {
            // Delete all related entries in topics_subject_data for this topic
            topicsSubjectDataRepository.deleteByTopicId(topicId);
            // If no other associations, delete the topic from the database
            topicRepository.findById(topicId).ifPresent(topicRepository::delete);
        }
    }
}
