package com.edutech.api.form.examanswer;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateExamAnswerForm {
    private Long question_id;
    private String answer;
    private Date answer_date;
    private Long exam_id;
}
