package com.example.trainerapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "topic")
@Data
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long topicId;

    @NotBlank(message = "Topic name cannot be empty")
    @Column(name = "topic_name", nullable = false)
    private String topicName;

    @Column(name = "description")
    private String description;
}
