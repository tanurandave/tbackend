package com.example.trainerapp.repository;

import com.example.trainerapp.entity.TopicsSubjectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TopicsSubjectDataRepository extends JpaRepository<TopicsSubjectData, Long> {

    List<TopicsSubjectData> findBySubjectId(Long subjectId);

    List<TopicsSubjectData> findByTrainerId(Long trainerId);

    List<TopicsSubjectData> findByTopicId(Long topicId);

    List<TopicsSubjectData> findByTrainerIdAndSubjectId(Long trainerId, Long subjectId);

    boolean existsBySubjectIdAndTopicIdAndTrainerId(Long subjectId, Long topicId, Long trainerId);

    void deleteBySubjectIdAndTopicIdAndTrainerId(Long subjectId, Long topicId, Long trainerId);

    void deleteBySubjectIdAndTopicId(Long subjectId, Long topicId);

    @Modifying
    @Transactional
    void deleteByTopicId(Long topicId);

    @Modifying
    @Transactional
    void deleteBySubjectId(Long subjectId);
}
