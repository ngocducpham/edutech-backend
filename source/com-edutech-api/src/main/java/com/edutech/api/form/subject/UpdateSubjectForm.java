package com.edutech.api.form.subject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Schema
public class UpdateSubjectForm {
    @NotNull
    @Schema(required = true)
    private Long id;
    private String name;
    private String description;
    private String code;
    private Integer status;
}
