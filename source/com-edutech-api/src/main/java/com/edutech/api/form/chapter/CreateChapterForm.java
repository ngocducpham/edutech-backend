package com.edutech.api.form.chapter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Schema
public class CreateChapterForm {
    @NotBlank
    @Schema(required = true)
    private String title;

    @NotNull
    @Schema(required = true)
    private Long syllabusId;

    private String description;
}
