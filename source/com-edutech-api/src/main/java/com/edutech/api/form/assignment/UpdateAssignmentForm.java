package com.edutech.api.form.assignment;

import com.edutech.api.validation.AssignmentType;
import com.edutech.api.validation.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateAssignmentForm {
    @NotNull
    private Long id;

    private String title;

    private String description;

    @Status
    private Integer status;
}
