package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Exam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ExamCriteria {
    private Date attempt_time;
    private Date submit_time;
    private Integer total_point;
    private String attachment;
    private Long studentId;
    private Long assignmentId;

    @Schema(hidden = true)
    public Specification<Exam> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(attempt_time != null){
                predicates.add(cb.equal(root.get("attempt_time"), attempt_time));
            }

            if (submit_time != null) {
                predicates.add(cb.equal(root.get("submit_time"), submit_time));
            }

            if (total_point != null) {
                predicates.add(cb.equal(root.get("total_point"), total_point));
            }

            if (attachment != null) {
                predicates.add(cb.equal(root.get("attachment"), attachment));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
