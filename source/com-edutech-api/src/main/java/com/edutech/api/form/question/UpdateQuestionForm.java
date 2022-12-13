package com.edutech.api.form.question;

import com.edutech.api.validation.QuestionType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateQuestionForm {

    private Long id;

    @NotBlank
    private String content;

    private String answer;

    private Integer point;

    @QuestionType
    private Integer type;
}
