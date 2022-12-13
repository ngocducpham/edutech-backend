package com.edutech.api.dto.exam;
import com.edutech.api.dto.examanswer.ExamAnswerDto;
import com.edutech.api.dto.question.QuestionDto;
import com.edutech.api.storage.model.ExamAnswer;
import com.edutech.api.storage.model.Question;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ExamDto {
    private Long id;
    private Date attempt_time;
    private Date submit_time;
    private int total_point;
    private String attachment;
    private Long studentId;
    private Long assignmentClassId;
    private List<QuestionDto> questionList;
    private List<ExamAnswerDto> answerList;
}
