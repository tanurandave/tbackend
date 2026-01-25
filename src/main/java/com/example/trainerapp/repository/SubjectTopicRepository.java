package com.example.trainerapp.repository;

import com.example.trainerapp.entity.SubjectTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubjectTopicRepository extends JpaRepository<SubjectTopic, Long> {

    List<SubjectTopic> findBySubjectId(Long subjectId);

    List<SubjectTopic> findByTopicId(Long topicId);

    boolean existsBySubjectIdAndTopicId(Long subjectId, Long topicId);

    void deleteBySubjectIdAndTopicId(Long subjectId, Long topicId);

    @Modifying
    @Transactional
    void deleteBySubjectId(Long subjectId);

    long countByTopicIdAndSubjectIdNot(Long topicId, Long subjectId);
}
