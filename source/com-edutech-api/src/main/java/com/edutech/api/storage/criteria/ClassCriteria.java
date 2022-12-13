package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Class;
import com.edutech.api.storage.model.Student;
import com.edutech.api.storage.model.Subject;
import com.edutech.api.storage.model.Teacher;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClassCriteria {

    private Long id;
    private String title;
    private Long subjectId;
    private Long teacherId;
    private Integer status;

    private Long studentId;

    @Schema(hidden = true)
    public Specification<Class> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(id != null){
                predicates.add(cb.equal(root.get("id"), id));
            }

            if(getStudentId() != null) {
                Join<Class, Student> studentJoin = root.join("students");
                predicates.add(cb.equal(studentJoin.get("id"), getStudentId()));
            }

            if (subjectId != null) {
                Join<Class, Subject> subjectJoin = root.join("subject");
                predicates.add(cb.equal(subjectJoin.get("subject").get("id"), subjectId));
            }

            if (teacherId != null) {
                Join<Class, Teacher> teacherJoin = root.join("teacher");
                predicates.add(cb.equal(teacherJoin.get("id"), teacherId));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if(studentId != null) {
                Join<Class, Student> studentJoin = root.join("students");
                predicates.add(cb.equal(studentJoin.get("id"), studentId));
            }

            if (StringUtils.isNotBlank(title)) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
