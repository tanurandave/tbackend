package com.example.trainerapp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class TrainerWithTopics {
    private Long empId;
    private String name;
    private String email;
    private int experience;
    private String address;
    private String format;
    private String mobileNumber;
    private List<String> topicNames;

    public TrainerWithTopics(Long empId, String name, String email, int experience, String address, String format, String mobileNumber, List<String> topicNames) {
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.experience = experience;
        this.address = address;
        this.format = format;
        this.mobileNumber = mobileNumber;
        this.topicNames = topicNames;
    }
}
