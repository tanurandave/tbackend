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

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}
