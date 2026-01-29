package com.example.trainerapp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopicWithTrainer {
    private Long topicId;
    private String topicName;
    private String description;
    private Long trainerId;
    private String trainerName;

    public TopicWithTrainer(Long topicId, String topicName, String description, Long trainerId, String trainerName) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.description = description;
        this.trainerId = trainerId;
        this.trainerName = trainerName;
    }
}
