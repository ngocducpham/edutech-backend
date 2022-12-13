package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {

    @Query("select q from Question q " +
            "where q.id = ?1 and q.assignment.lesson.chapter.syllabus.teacher.id = ?2")
    Optional<Question> findByIdAndTeacherId(Long id, Long teacherId);

    @Query("select q from Question q " +
            "where q.assignment.id = ?1")
    Page<Question> findAllByAssignmentId(Long id, Pageable pageable);

    @Query("select q from Question q " +
            "where q.assignment.id = ?1")
    List<Question> findAllByAssignmentIdList(Long id);
}
