package com.example.trainerapp.entity;

import lombok.Data;
import java.util.List;

@Data
public class SubjectRequest {
    private String subjectName;
    private String description;
    private List<Topic> topics;
}
