package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {
    @Query("select l from " +
            "Lesson l " +
            "where l.id = ?1 and l.chapter.syllabus.teacher.id = ?2")
    Optional<Lesson> findByIdAndTeacherId(Long id, Long teacherId);

    List<Lesson> findAllByChapterIdOrderByOrder(Long chapterId);

    @Query("select l from Lesson l where l.chapter.id=?1")
    Page<Lesson> findAllByChapterId (Long id, Pageable pageable);
}
