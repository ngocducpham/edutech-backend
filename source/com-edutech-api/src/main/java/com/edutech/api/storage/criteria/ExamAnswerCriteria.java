package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Exam;
import com.edutech.api.storage.model.ExamAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ExamAnswerCriteria {
    private Long question_id;
    private String answer;
    private Integer point;
    private Date answer_date;
    private Long exam_id;

    @Schema(hidden = true)
    public Specification<ExamAnswer> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<ExamAnswer, Exam> answerExamJoin = root.join("exam");
            if(question_id != null){
                predicates.add(cb.equal(root.get("question_id"), question_id));
            }

            if (answer != null) {
                predicates.add(cb.equal(root.get("answer"), answer));
            }

            if (point != null) {
                predicates.add(cb.equal(root.get("point"), point));
            }

            if (answer_date != null) {
                predicates.add(cb.equal(root.get("answer_date"), answer_date));
            }

            if (exam_id != null) {
                predicates.add(cb.equal(answerExamJoin.get("id"), exam_id));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
