package com.example.trainerapp.entity;

import java.util.List;

public class SubjectWithTrainers {
    private Subject subject;
    private List<Trainer> trainers;

    public SubjectWithTrainers(Subject subject, List<Trainer> trainers) {
        this.subject = subject;
        this.trainers = trainers;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers;
    }
}
