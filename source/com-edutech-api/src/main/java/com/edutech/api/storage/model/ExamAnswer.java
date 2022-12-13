package com.edutech.api.storage.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE+"answer")
@Getter
@Setter
public class ExamAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @Column(columnDefinition = "LONGTEXT")
    private String answer;
    private Double point;
    private Date answer_date;
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;
}
