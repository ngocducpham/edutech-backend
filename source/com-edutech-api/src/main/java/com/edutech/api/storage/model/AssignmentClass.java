package com.edutech.api.storage.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE+"assigment_class")
//@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class AssignmentClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date start;
    private Date end;
    private int duration;
    private int max_attempt;
    private int status;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class aClass;
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;
    private int type;

}
