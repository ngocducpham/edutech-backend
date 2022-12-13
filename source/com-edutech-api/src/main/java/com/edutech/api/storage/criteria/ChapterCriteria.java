package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Chapter;
import com.edutech.api.storage.model.Syllabus;
import com.edutech.api.storage.model.Teacher;
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
public class ChapterCriteria {
    @NotNull
    private Long syllabusId;

    @Schema(hidden = true)
    private Long teacherId;

    @Schema(hidden = true)
    public Specification<Chapter> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Chapter, Syllabus> syllabusJoin = root.join("syllabus");

            if (syllabusId != null) {
                predicates.add(cb.equal(syllabusJoin.get("id"), syllabusId));
            }

            if(teacherId != null){
                Join<Syllabus, Teacher> teacherJoin = syllabusJoin.join("teacher");
                predicates.add(cb.equal(teacherJoin.get("id"), teacherId));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
