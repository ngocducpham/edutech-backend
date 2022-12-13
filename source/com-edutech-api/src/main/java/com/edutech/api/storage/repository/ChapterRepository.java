package com.edutech.api.storage.repository;


import com.edutech.api.storage.model.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long>, JpaSpecificationExecutor<Chapter> {
    @Query("select c " +
            "from Chapter c inner join c.syllabus s inner join s.teacher t " +
            "where c.id = ?1 and t.id = ?2")
    Optional<Chapter> findByIdAndTeacherId(Long id, Long teacherId);

    @Query("select c " +
            "from Chapter c inner join c.syllabus s " +
            "where c.id = ?1 and s.id = ?2")
    Optional<Chapter> findByIdAndSyllabusId(Long id, Long syllabusId);

    @Query("select c from Chapter c where c.syllabus.id=?1")
    Page<Chapter> findAllBySyllabusId (Long id, Pageable pageable);
}
