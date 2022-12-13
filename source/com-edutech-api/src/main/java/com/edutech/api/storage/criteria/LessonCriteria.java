package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Chapter;
import com.edutech.api.storage.model.Lesson;
import com.edutech.api.storage.model.Syllabus;
import com.edutech.api.storage.model.Teacher;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class LessonCriteria {
    @NotNull
    private Long chapterId;

    @Schema(hidden = true)
    private Long teacherId;

    @Schema(hidden = true)
    public Specification<Lesson> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Lesson, Chapter> chapterJoin = root.join("chapter");

            if(teacherId != null){
                Join<Chapter, Syllabus> syllabusJoin = chapterJoin.join("syllabus");
                Join<Syllabus, Teacher> teacherJoin = syllabusJoin.join("teacher");
                predicates.add(cb.equal(teacherJoin.get("id"), teacherId));
            }

            if (chapterId != null) {
                predicates.add(cb.equal(chapterJoin.get("id"), chapterId));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
