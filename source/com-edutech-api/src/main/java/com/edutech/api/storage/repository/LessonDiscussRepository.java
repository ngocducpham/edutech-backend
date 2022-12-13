package com.edutech.api.storage.repository;

import com.edutech.api.storage.criteria.LessonDiscussCriteria;
import com.edutech.api.storage.model.LessonDiscuss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LessonDiscussRepository extends JpaRepository<LessonDiscuss, Long> {
    Page<LessonDiscuss> findAll(Specification<LessonDiscuss> specification, Pageable pageable);

    @Query("SELECT ld " +
            "FROM LessonDiscuss ld join ld.aclass c " +
            "WHERE c.id = ?1 order by ld.created desc")
    Page<LessonDiscuss> findAllByAclassId(Long classId, Pageable pageable);
    @Query("SELECT ld " +
            "FROM LessonDiscuss ld join ld.lesson l " +
            "WHERE l.id = ?1 order by ld.created desc")
    Page<LessonDiscuss> findAllByLessonId(Long lessonId, Pageable pageable);
}
