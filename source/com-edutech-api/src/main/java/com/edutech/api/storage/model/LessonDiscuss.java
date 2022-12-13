package com.edutech.api.storage.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE+"lessondiscuss")
@Getter
@Setter
public class LessonDiscuss {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne()
    @JoinColumn(name = "class_id")
    private Class aclass;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private Account account;

    private Date created;

    @ManyToOne()
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    private Integer status;

    @OneToMany(mappedBy = "discuss", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiscussComment> discusscomments;
}
