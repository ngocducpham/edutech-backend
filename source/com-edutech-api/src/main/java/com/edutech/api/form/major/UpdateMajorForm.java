package com.edutech.api.form.major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Schema
public class UpdateMajorForm {
    @NotNull
    @Schema(required = true)
    private Long id;
    private String name;
    private String description;
    private String image;
    private Integer status;
}
