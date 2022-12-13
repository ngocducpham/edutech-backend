package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SyllabusRepository extends JpaRepository<Syllabus,Long>, JpaSpecificationExecutor<Syllabus> {
    List<Syllabus> findAllByTeacherId(Long teacherId);

    Optional<Syllabus> findByIdAndTeacherId(Long id, Long teacherId);

    @Query("select s " +
            "from Syllabus s " +
            "where s.id = ?1 and s.teacher.id = ?2 and s.subject.id = ?3")
    Optional<Syllabus> findByIdAndTeacherIdAndSubjectId(Long id, Long teacherId, Long subjectId);

    @Query("select s " +
            "from Syllabus s join s.chapters c " +
            "where s.id = ?1 and c.id = ?2")
    Optional<Syllabus> findByIdAndChapterId(Long id, Long chapterId);
}
