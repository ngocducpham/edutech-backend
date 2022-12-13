package com.edutech.api.form.question;

import com.edutech.api.validation.QuestionType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateQuestionForm {
    @NotNull
    private Long assignmentId;

    @NotBlank
    private String content;

    private String answer;

    private Integer point;

    @QuestionType
    private Integer type;
}
