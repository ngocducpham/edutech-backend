package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Assignment;
import com.edutech.api.storage.model.Chapter;
import com.edutech.api.storage.model.Lesson;
import com.edutech.api.storage.model.Syllabus;
import com.edutech.api.validation.AssignmentType;
import com.edutech.api.validation.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AssignmentCriteria {

    private String title;

    private Long lessonId;

    private Long syllabusId;

    private Integer status;

    @AssignmentType(allowNull = true)
    private Integer type;

    @Schema(hidden = true)
    private Long teacherId;


    @Schema(hidden = true)
    public Specification<Assignment> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(type != null){
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (title != null) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (syllabusId != null){
                predicates.add(cb.equal(root.get("lesson")
                        .get("chapter").get("syllabus").get("id"), syllabusId));
            }

            if (teacherId != null){
                predicates.add(cb.equal(root.get("lesson")
                        .get("chapter").get("syllabus").get("teacher").get("id"), teacherId));
            }

            if(lessonId != null){
                predicates.add(cb.equal(root.get("lesson").get("id"), lessonId));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
