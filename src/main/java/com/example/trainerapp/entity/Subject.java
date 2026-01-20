package com.example.trainerapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "subject")
@Data
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    private String subjectName;
    private String description;
}
