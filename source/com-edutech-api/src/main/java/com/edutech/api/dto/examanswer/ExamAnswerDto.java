package com.edutech.api.dto.examanswer;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExamAnswerDto {
    private Long id;
    private Long question_id;
    private String answer;
    private Integer point;
    private Date answer_date;
    private Long exam_id;
}
