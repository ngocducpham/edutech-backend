package com.edutech.api.dto.question;

import com.edutech.api.dto.assignment.AssignmentDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto {
    private Long id;
    private String content;
    private String answer;
    private Integer point;
    private Integer type;
    private AssignmentDto assignment;
}
