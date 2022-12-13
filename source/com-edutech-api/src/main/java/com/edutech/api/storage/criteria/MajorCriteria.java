package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MajorCriteria {
    private String name;
    private Integer status;

    @Schema(hidden = true)
    public Specification<Major> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(StringUtils.isNoneEmpty(name)){
                predicates.add(cb.like(cb.lower(root.get("name")), "%"+name.toLowerCase()+"%"));
            }

            if(status != null){
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
