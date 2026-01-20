package com.example.trainerapp.repository;

import com.example.trainerapp.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    @Query("""
        SELECT t FROM Trainer t
        JOIN TrainerSubject ts ON t.empId = ts.empId
        JOIN Subject s ON ts.subjectId = s.subjectId
        WHERE s.subjectName = :subjectName
    """)
    List<Trainer> findTrainersBySubjectName(@Param("subjectName") String subjectName);
}
