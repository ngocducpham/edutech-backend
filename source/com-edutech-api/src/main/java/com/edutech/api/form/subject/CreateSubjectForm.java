package com.edutech.api.form.subject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Schema
public class CreateSubjectForm {
    @Schema(required = true)
    @NotBlank
    private String name;

    @Schema(required = true)
    @NotBlank
    private String description;

    @Schema(required = true)
    @NotBlank
    private String code;
}
