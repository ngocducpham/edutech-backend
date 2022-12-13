package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotNull;
import java.lang.Class;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LessonDiscussCriteria {
    private Long id;
    private Long classId;
    private Long userId;

    private Long lessonId;
    private Integer status;

    @Schema(hidden = true)
    public Specification<LessonDiscuss> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(id != null){
                predicates.add(cb.equal(root.get("id"), id));
            }

            if (classId != null) {
                Join<LessonDiscuss, Class> classJoin = root.join("class");
                predicates.add(cb.equal(classJoin.get("id"), classId));
            }
            if (userId != null) {
                Join<LessonDiscuss, Account> accountJoin = root.join("account");
                predicates.add(cb.equal(accountJoin.get("id"), userId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
