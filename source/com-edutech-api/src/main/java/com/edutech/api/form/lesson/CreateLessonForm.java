package com.edutech.api.form.lesson;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateLessonForm {
    @NotEmpty(message = "title cannot be null")
    @Schema(required = true)
    private String title;

    private String icon;

    @NotNull
    @Schema(required = true)
    private Long order;

    private String attachment;

    @NotEmpty(message = "content cannot be null")
    @Schema(required = true)
    private String content;

    @NotNull
    @Schema(required = true)
    private Long chapterId;
}
