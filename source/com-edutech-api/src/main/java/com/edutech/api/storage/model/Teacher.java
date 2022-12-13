package com.edutech.api.storage.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE+"teacher")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String degree;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "account_id")
    @MapsId
    private Account account;

    private String address;

    @ManyToOne()
    @JoinColumn(name = "province_id")
    private Province province;

    @ManyToOne()
    @JoinColumn(name = "district_id")
    private Province district;

    @ManyToOne()
    @JoinColumn(name = "commune_id")
    private Province commune;

    @ManyToMany
    @JoinTable(name = TablePrefix.PREFIX_TABLE + "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects = new ArrayList<>();
}
