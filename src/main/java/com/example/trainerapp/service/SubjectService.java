package com.example.trainerapp.service;

import com.example.trainerapp.entity.*;
import com.example.trainerapp.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final SubjectTopicRepository subjectTopicRepository;

    public SubjectService(
            SubjectRepository subjectRepository,
            TopicRepository topicRepository,
            SubjectTopicRepository subjectTopicRepository
    ) {
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.subjectTopicRepository = subjectTopicRepository;
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

    public SubjectWithTrainers getSubjectWithTrainers(Long id) {
        Subject subject = subjectRepository.findById(id).orElse(null);
        return subject == null ? null : new SubjectWithTrainers(subject, List.of());
    }
}
