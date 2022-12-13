package com.edutech.api.form.classform;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateClassForm {
    @NotBlank
    private String title;

    private String avatar;

    private String description;

    @NotNull
    private Long teacherId;

    @NotNull
    private Long subjectId;
}
