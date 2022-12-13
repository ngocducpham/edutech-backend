package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Assignment;
import com.edutech.api.storage.model.Question;
import com.edutech.api.validation.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionCriteria {

    @NotNull
    private Long assignmentId;

    private String content;

    @QuestionType(allowNull = true)
    private Integer type;

    @Schema(hidden = true)
    private Long teacherId;

    @Schema(hidden = true)
    public Specification<Question> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (assignmentId != null) {
                predicates.add(cb.equal(root.get("assignment").get("id"), assignmentId));
            }

            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (content != null) {
                predicates.add(cb.like(cb.lower(root.get("content")), "%" + content.toLowerCase() + "%"));
            }

            if(teacherId != null){
                predicates.add(cb.equal(root.get("assignment")
                        .get("lesson").get("chapter").get("syllabus").get("teacher").get("id"), teacherId));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
