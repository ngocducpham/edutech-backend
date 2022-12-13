package com.edutech.api.storage.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE+"student")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    private String admissionYear;

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

    @ManyToMany(mappedBy = "students")
    private List<Class> classes;
}
