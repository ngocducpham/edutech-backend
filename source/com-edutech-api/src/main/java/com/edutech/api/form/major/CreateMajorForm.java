package com.edutech.api.form.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Schema
public class CreateMajorForm {
    @Schema(required = true)
    @NotBlank
    private String name;

    @Schema(required = true)
    @NotBlank
    private String description;

    @Schema(required = true)
    @NotBlank
    private String image;
}
