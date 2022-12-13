package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Subject;
import com.edutech.api.storage.model.Syllabus;
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
public class SyllabusCriteria {
    private String title;

    @Schema(hidden = true)
    private Long teacherId;

    private Long subjectId;

    @Schema(hidden = true)
    public Specification<Syllabus> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNoneEmpty(title)) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (teacherId != null) {
                Join<Syllabus, Teacher> teacherJoin = root.join("teacher");
                predicates.add(cb.equal(teacherJoin.get("id"), teacherId));
            }

            if (subjectId != null) {
                Join<Syllabus, Subject> subjectJoin = root.join("subject");
                predicates.add(cb.equal(subjectJoin.get("id"), subjectId));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
