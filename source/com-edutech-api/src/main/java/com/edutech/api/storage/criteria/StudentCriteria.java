package com.edutech.api.storage.criteria;

import com.edutech.api.storage.model.Account;
import com.edutech.api.storage.model.Class;
import com.edutech.api.storage.model.Major;
import com.edutech.api.storage.model.Student;
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
public class StudentCriteria {
    private String fullName;
    private Integer status;
    private Long classId;
    private Long teacherId;
    private Long majorId;

    @Schema(hidden = true)
    public Specification<Student> getSpecification() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Student, Account> accountJoin = root.join("account");

            if(classId != null){
                Join<Student, Class> classJoin = root.join("classes");
                predicates.add(cb.equal(classJoin.get("id"), classId));
            }

            if(teacherId != null){
                Join<Student, Class> classJoin = root.join("classes");
                predicates.add(cb.equal(classJoin.get("teacher").get("id"), teacherId));
            }

            if(StringUtils.isNoneEmpty(fullName)){
                predicates.add(cb.like(cb.lower(accountJoin.get("fullName")), "%"+fullName.toLowerCase()+"%"));
            }

            if(majorId != null){
                Join<Student, Major> majorJoin = root.join("major");
                predicates.add(cb.equal(majorJoin.get("id"), majorId));
            }

            if(status != null){
                predicates.add(cb.equal(accountJoin.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
