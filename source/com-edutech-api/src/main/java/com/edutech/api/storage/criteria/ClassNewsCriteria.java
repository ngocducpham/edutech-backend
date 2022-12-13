package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Class;
import com.edutech.api.storage.model.ClassNews;
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
public class ClassNewsCriteria {
    private Long id;
    private String content;
    private Integer status;
    private Long classId;

    private String image;

    @Schema(hidden = true)
    public Specification<ClassNews> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(id != null){
                predicates.add(cb.equal(root.get("id"), id));
            }

            if(image != null){
                predicates.add(cb.equal(root.get("image"), image));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if(classId != null) {
                predicates.add(cb.equal(root.get("class"), classId));
            }

            if (StringUtils.isNotBlank(content)) {
                predicates.add(cb.like(cb.lower(root.get("content")), "%" + content.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
