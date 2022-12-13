package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long>, JpaSpecificationExecutor<Assignment> {

    @Query("select a " +
            "from Assignment a " +
            "where a.id = ?1 and a.lesson.chapter.syllabus.teacher.id = ?2")
    Optional<Assignment> findByIdAndTeacherId(Long id, Long teacherId);
}
