package com.edutech.api.form.syllabus;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SyllabusForm {
    @NotBlank
    private String title;

    private String description;

    private String avatar;

    @NotNull
    private Long subjectId;
}
