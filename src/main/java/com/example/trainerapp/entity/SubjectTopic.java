package com.example.trainerapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "subject_topic")
@Data
public class SubjectTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "topic_id")
    private Long topicId;
}
