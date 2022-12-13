package com.edutech.api.form.lesson;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateLessonForm {

    @NotNull(message = "id cannot be null")
    @Schema(required = true)
    private Long id;

    private String title;

    private String icon;

    private Long order;

    private String attachment;

    private String content;
}
