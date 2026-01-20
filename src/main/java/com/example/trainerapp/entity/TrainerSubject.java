package com.example.trainerapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "trainer_subject",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"emp_id", "subject_id"})
    }
)
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_id", nullable = false)
    private Long empId;
    
    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(name = "trainer_name", nullable = false)
    private String trainerName;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;
}
