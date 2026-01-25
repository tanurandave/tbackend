package com.example.trainerapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "topics_subject_data")
@Data
public class TopicsSubjectData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "trainer_id")
    private Long trainerId;

    @Column(name = "topic_id")
    private Long topicId;
}
