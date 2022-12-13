package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.ExamAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamAnswerRepository extends JpaRepository<ExamAnswer, Long> {
    Page<ExamAnswer> findAll(Specification<ExamAnswer> specification, Pageable pageable);

    @Query("SELECT ea FROM ExamAnswer ea WHERE ea.exam.id =?1")
    Page<ExamAnswer> findAllByExamId(Long examId, Pageable pageable);

    @Query("SELECT ea FROM ExamAnswer ea WHERE ea.exam.id =?1")
    Page<ExamAnswer> findAllByExamIdAndStudentId(Long examId, Pageable pageable);

    @Query("SELECT ea FROM ExamAnswer ea WHERE ea.exam.id =?1")
    List<ExamAnswer> findAllByExamId(Long examId);

    @Query("SELECT ea FROM ExamAnswer ea WHERE ea.exam.id=?1 and ea.question.id=?2")
    ExamAnswer findByExamIdAndQuestionId(Long examId, Long questionId);
}
