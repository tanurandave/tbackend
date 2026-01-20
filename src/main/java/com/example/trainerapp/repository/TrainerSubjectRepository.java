
package com.example.trainerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.example.trainerapp.entity.TrainerSubject;
import java.util.List;
import java.util.Optional;

public interface TrainerSubjectRepository 
        extends JpaRepository<TrainerSubject, Long> {
    
    /**
     * Find all assignments by trainer
     */
    List<TrainerSubject> findByEmpId(Long empId);
    
    /**
     * Find all assignments by subject
     */
    List<TrainerSubject> findBySubjectId(Long subjectId);
    
    /**
     * Find specific assignment by trainer and subject
     * Uses LIMIT 1 to ensure only one result
     */
    @Query(value = "SELECT * FROM trainer_subject WHERE emp_id = :empId AND subject_id = :subjectId LIMIT 1", 
           nativeQuery = true)
    Optional<TrainerSubject> findByEmpIdAndSubjectId(@Param("empId") Long empId, @Param("subjectId") Long subjectId);
    
    /**
     * Delete assignment by trainer and subject
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM TrainerSubject ts WHERE ts.empId = :empId AND ts.subjectId = :subjectId")
    int deleteByEmpIdAndSubjectId(@Param("empId") Long empId, @Param("subjectId") Long subjectId);
    
    /**
     * Check if assignment exists
     */
    boolean existsByEmpIdAndSubjectId(Long empId, Long subjectId);
    
    /**
     * Delete all assignments for a subject
     */
    @Modifying
    @Transactional
    void deleteBySubjectId(Long subjectId);

    /**
     * Delete all duplicates keeping only the first one
     * Used for data cleanup
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM trainer_subject WHERE id NOT IN (" +
                   "SELECT MIN(id) FROM trainer_subject GROUP BY emp_id, subject_id" +
                   ")", nativeQuery = true)
    int deleteDuplicates();
}
