package com.edutech.api.form.assignment;

import com.edutech.api.validation.AssignmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateAssignmentForm {
    @NotNull
    private Long lessonId;

    @NotBlank
    private String title;

    private String description;

    @Schema(required = true)
    @AssignmentType
    private Integer type;
}
