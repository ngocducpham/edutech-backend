package com.edutech.api.storage.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE+"discusscomment")
@Getter
@Setter
public class DiscussComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Account user;

    @Column(name = "created")
    private Date posted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private DiscussComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<DiscussComment> childComment;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "discuss_id")
    private LessonDiscuss discuss;
}
