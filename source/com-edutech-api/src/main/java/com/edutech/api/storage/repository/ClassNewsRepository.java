package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.ClassNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClassNewsRepository extends JpaRepository<ClassNews, Long>, JpaSpecificationExecutor<ClassNews> {
    @Query("SELECT cn " +
            "FROM ClassNews cn join cn.aClass c " +
            "WHERE cn.id = ?1 AND c.id = ?2 ORDER BY cn.created DESC")
    Optional<ClassNews> findByIdAndClassIdAndStatus(Long classnewsId, long classId, Integer statusActive);

    @Query("SELECT cn " +
            "FROM ClassNews cn join cn.aClass c " +
            "WHERE c.id = ?1 ORDER BY cn.created DESC")
    Page<ClassNews> findAllByClassId(long classId, Pageable pageable);
    @Query("SELECT cn " +
            "FROM ClassNews cn join cn.aClass c " +
            "WHERE c.teacher.id = ?1 ORDER BY cn.created DESC")
    Page<ClassNews> findAllByTeacherId(long currentUserId, Pageable pageable);

    @Query("SELECT cn " +
            "FROM ClassNews cn join cn.aClass c join c.students s " +
            "WHERE s.id = ?1 ORDER BY cn.created DESC")
    Page<ClassNews> findAllByStudentId(long currentUserId, Pageable pageable);

}
