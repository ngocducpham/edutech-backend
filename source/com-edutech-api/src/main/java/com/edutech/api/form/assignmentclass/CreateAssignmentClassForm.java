package com.edutech.api.form.assignmentclass;

import com.edutech.api.validation.AssignmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class CreateAssignmentClassForm {
    private Long classId;
    private Long assignmentId;
    private int duration;
    private int max_attempt;
    private int status;
    private Integer type;
}
