package com.edutech.api.form.examanswer;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateExamAnswerForm {
    private Long id;
    private Long question_id;
    private String answer;
    private Integer point;
    private Date answer_date;
    private Long exam_id;
}
