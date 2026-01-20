package com.example.trainerapp.entity;

import lombok.Data;
import java.util.List;

@Data
public class SubjectTopicRequest {

    private List<Long> topicIds;      // existing topics
    private List<Topic> topics;       // new topics
}
