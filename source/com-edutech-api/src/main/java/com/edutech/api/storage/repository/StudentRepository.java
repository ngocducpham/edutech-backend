package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository <Student, Long>, JpaSpecificationExecutor<Student> {
    @Query("select s " +
            "from Student s " +
            "where s.id in (?1) and s.account.status = ?2")
    List<Student> findAllByIdAndStatus(List<Long> id, Integer status);
}
