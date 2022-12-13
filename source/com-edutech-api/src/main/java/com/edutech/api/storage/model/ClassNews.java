package com.edutech.api.storage.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE+"classnews")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ClassNews {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imageURL;
    private String content;
    private boolean pintop;
    private Date created;
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class aClass;
}
