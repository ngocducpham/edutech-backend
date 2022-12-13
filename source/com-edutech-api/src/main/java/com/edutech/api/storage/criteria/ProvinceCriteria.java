package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Province;
import com.edutech.api.validation.ProvinceKind;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProvinceCriteria {
    private String provinceName;

    private Long parentId;

    @ProvinceKind(allowNull = true)
    private String kind;

    @Schema(hidden = true)
    public Specification<Province> getSpecification() {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Province> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getProvinceName() != null) {
                    predicates.add(cb.like(cb.lower(root.get("provinceName")), "%" + getProvinceName().toLowerCase() + "%"));
                }

                if(getKind() != null) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }

                if(getParentId() != null) {
                    Join<Province, Province> joinParent = root.join("parentProvince");
                    predicates.add(cb.equal(joinParent.get("provinceId"), getParentId()));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
