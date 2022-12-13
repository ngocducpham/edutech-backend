package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long>, JpaSpecificationExecutor<Subject> {
    @Query("select s " +
            "from Subject s join s.teachers t " +
            "where s.id = ?1 and t.id = ?2")
    Optional<Subject> findByIdAndTeacherId(Long id ,Long teacherId);

    @Query("select s from Subject s " +
            "where s.id in (?1) and s.status = ?2")
    List<Subject> findAllByIdAndStatus(List<Long> id, Integer status);
}
