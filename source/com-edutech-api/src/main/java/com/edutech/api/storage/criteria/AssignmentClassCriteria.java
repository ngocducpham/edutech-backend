package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.AssignmentClass;
import com.edutech.api.validation.AssignmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AssignmentClassCriteria {
    private Long classId;
    private Integer status;
    @AssignmentType(allowNull = true)
    private Integer type;
    private Long assignmentId;


    @Schema(hidden = true)
    public Specification<AssignmentClass> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(type != null){
                predicates.add(cb.equal(root.get("type"), type));
            }

            if(classId != null){
                predicates.add(cb.equal(root.get("class_id"), classId));
            }

            if(assignmentId != null){
                predicates.add(cb.equal(root.get("assignment_id"), assignmentId));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
