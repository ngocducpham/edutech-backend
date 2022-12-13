package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class TeacherCriteria {
    private Long id;
    private String fullName;
    private Integer status;
    private String degree;
    private String address;
    private Long account_id;
    private Long province_id;
    private Long district_id;
    private Long commune_id;
    private Long subjectId;

    public Specification<Teacher> getSpecification() {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getSubjectId() != null){
                    Join<Teacher, Subject> subjectJoin = root.join("subjects");
                    predicates.add(cb.equal(subjectJoin.get("id"), getSubjectId()));
                }

                if(getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }

                if (StringUtils.isNoneEmpty(getFullName())) {
                    Join<Teacher, Account> accountJoin = root.join("account", JoinType.INNER);
                    predicates.add(cb.like(cb.lower(accountJoin.get("fullName")), "%"+fullName.toLowerCase()+"%"));
                }

                if(getStatus() != null) {
                    Join<Teacher, Account> accountJoin = root.join("account", JoinType.INNER);
                    predicates.add(cb.equal(accountJoin.get("status"), getStatus()));
                }

                if(getDegree() != null) {
                    predicates.add(cb.equal(root.get("degree"), getDegree()));
                }

                if(getAddress() != null) {
                    predicates.add(cb.equal(root.get("address"), getAddress()));
                }

                if(getProvince_id() != null) {
                    predicates.add(cb.equal(root.get("province_id"), getProvince_id()));
                }

                if(getDistrict_id() != null) {
                    predicates.add(cb.equal(root.get("district_id"), getDistrict_id()));
                }

                if(getCommune_id() != null) {
                    predicates.add(cb.equal(root.get("commune_id"), getCommune_id()));
                }

                if(getAccount_id() != null) {
                    Join<News, Category> joinCategory = root.join("account", JoinType.INNER);
                    predicates.add(cb.equal(joinCategory.get("id"), getAccount_id()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
