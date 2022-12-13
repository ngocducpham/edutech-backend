package com.edutech.api.form.classform;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateClassForm {
    @NotNull
    private Long id;

    private String title;

    private String avatar;

    private String description;

    private Long teacherId;

    private Long subjectId;

    private Long syllabusId;

    private Integer status;

}
