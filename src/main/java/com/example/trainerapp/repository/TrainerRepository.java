package com.example.trainerapp.repository;

import com.example.trainerapp.entity.Trainer;
import com.example.trainerapp.entity.TrainerWithTopics;
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

    @Query("""
        SELECT t FROM Trainer t
        JOIN TrainerSubject ts ON t.empId = ts.empId
        WHERE ts.subjectId = :subjectId
    """)
    List<Trainer> findTrainersBySubjectId(@Param("subjectId") Long subjectId);

    @Query(value = """
        SELECT t.emp_id, t.name, t.email, t.experience, t.address, t.format, t.mobile_number, JSON_ARRAYAGG(tp.topic_name) as topic_names
        FROM trainer t
        JOIN trainer_subject ts ON t.emp_id = ts.emp_id
        LEFT JOIN topics_subject_data tsd ON ts.subject_id = tsd.subject_id AND t.emp_id = tsd.trainer_id
        LEFT JOIN topic tp ON tsd.topic_id = tp.topic_id
        WHERE ts.subject_id = :subjectId
        GROUP BY t.emp_id, t.name, t.email, t.experience, t.address, t.format, t.mobile_number
    """, nativeQuery = true)
    List<Object[]> findTrainersWithTopicsBySubjectId(@Param("subjectId") Long subjectId);
}
