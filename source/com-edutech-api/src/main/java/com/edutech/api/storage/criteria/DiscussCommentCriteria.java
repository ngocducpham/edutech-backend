package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Account;
import com.edutech.api.storage.model.DiscussComment;
import com.edutech.api.storage.model.Lesson;
import com.edutech.api.storage.model.LessonDiscuss;
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
public class DiscussCommentCriteria {
    private Long id;
    private Long discussId;
    private Long userId;
    private Long parentId;


    @Schema(hidden = true)
    public Specification<DiscussComment> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(id != null){
                predicates.add(cb.equal(root.get("id"), id));
            }

            if (discussId != null) {
                Join<Lesson, LessonDiscuss> discussJoinJoin = root.join("discuss");
                predicates.add(cb.equal(discussJoinJoin.get("id"), discussId));
            }
            if (userId != null) {
                Join<Lesson, Account> userJoin = root.join("account");
                predicates.add(cb.equal(userJoin.get("id"), userId));
            }
            if (parentId != null) {
                Join<Lesson, DiscussComment> commentJoin = root.join("parent");
                predicates.add(cb.equal(commentJoin.get("id"), parentId));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
