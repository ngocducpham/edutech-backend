package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.AssignmentClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface AssignmentClassRepository extends JpaRepository<AssignmentClass, Long>, JpaSpecificationExecutor<AssignmentClass> {
    @Query("SELECT ac " +
            "FROM AssignmentClass ac " +
            "WHERE ac.id=?1 AND ac.status=?2 ORDER BY ac.assignment.lesson.id ASC")
    AssignmentClass findByIdAndStatus(Long id, Integer status);

    @Query("select ac from AssignmentClass ac " +
            "LEFT JOIN Exam e ON e.assignmentClass.id = ac.id " +
            "where e.student.id =?1 and ac.aClass.id=?2 " +
            "and ac.status=1 ORDER BY ac.assignment.lesson.id ASC")
    Page<AssignmentClass> findAllByStudentIdAAndClassIdAndStatus(Long studentId, Long classId, Pageable pageable);

    @Query("select ac from AssignmentClass ac " +
            "where ac.aClass.id=?1 ORDER BY ac.assignment.lesson.id ASC")
    Page<AssignmentClass> findAllByClassId(Long classId, Pageable pageable);

    @Query("select ac from AssignmentClass ac " +
            "where ac.assignment.lesson.id =?1 and ac.aClass.id =?2")
    Page<AssignmentClass> findAllByLessonId(Long lessonId, Long classId, Pageable pageable);
}
