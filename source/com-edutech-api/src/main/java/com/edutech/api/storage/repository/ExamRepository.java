package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Page<Exam> findAll(Specification<Exam> specification, Pageable pageable);
    @Query("SELECT e FROM Exam e WHERE e.assignmentClass.id=?1")
    Page<Exam> findAllByAssignmentClassId(Long id, Pageable pageable);
    @Query("SELECT e FROM Exam e WHERE e.assignmentClass.id=?1 AND e.student.id=?2")
    Page<Exam> findAllByAssignmentClassIdAndStudentId(Long assignmentId, Long studentId, Pageable pageable);

    @Query("SELECT e FROM Exam e WHERE e.assignmentClass.id=?1 AND e.student.id=?2")
    List<Exam> findAllByAssignmentClassIdAndStudentIdList(Long assignmentId, Long studentId);
}
