package com.edutech.api.form.chapter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Schema
public class UpdateChapterForm {
    @NotNull
    @Schema(required = true)
    private Long id;

    private String title;

    private String description;
}
