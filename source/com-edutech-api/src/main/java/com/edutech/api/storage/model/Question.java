package com.edutech.api.storage.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE+"question")
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "LONGTEXT", name = "question_content")
    private String content;

    @Column(columnDefinition = "LONGTEXT")
    private String answer;

    @Column(name = "_point")
    private Double point;

    private Integer type;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;
}
