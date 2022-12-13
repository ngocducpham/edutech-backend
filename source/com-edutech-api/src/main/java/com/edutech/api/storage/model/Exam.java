package com.edutech.api.storage.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE+"exam")
@Getter
@Setter
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date attempt_time;
    private Date submit_time;
    private int total_point;
    private String attachment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private AssignmentClass assignmentClass;

    @OneToMany(mappedBy = "exam")
    private List<ExamAnswer> answerList;


}
