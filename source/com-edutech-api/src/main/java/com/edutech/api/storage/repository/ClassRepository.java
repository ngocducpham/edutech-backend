package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long>, JpaSpecificationExecutor<Class> {
    Optional<Class> findByIdAndTeacherIdAndStatus(Long id, Long teacherId, Integer status);
    @Query("SELECT c " +
            "FROM Class c join c.students s " +
            "WHERE c.id = ?1 AND s.id = ?2")
    Optional<Class> findByIdAndStudentIdAndStatus(Long id, Long studentId, Integer status);

    Optional<Class> findBySyllabusId(Long syllabusId);
}
