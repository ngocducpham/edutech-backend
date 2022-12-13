package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Subject;
import com.edutech.api.storage.model.Teacher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubjectCriteria {
    private String name;
    private String code;
    private Integer status;
    private Long teacherId;

    @Schema(hidden = true)
    public Specification<Subject> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
            }

            if (getCode() != null) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + getCode().toLowerCase() + "%"));
            }

            if (getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), getStatus()));
            }

            if (getTeacherId() != null) {
                Join<Subject, Teacher> joinTeacher = root.join("teachers", JoinType.INNER);
                predicates.add(cb.equal(joinTeacher.get("id"), getTeacherId()));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
