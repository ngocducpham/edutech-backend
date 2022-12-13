package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    @Query("select t " +
            "from Teacher t join t.subjects s " +
            "where t.id = ?1 and s.id = ?2")
    Optional<Teacher> findByIdAndSubjectId(Long id, Long subjectId);
}
