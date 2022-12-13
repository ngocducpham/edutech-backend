package com.edutech.api.form.assignmentclass;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class UpdateAssignmentClassForm {
    @NotNull
    private Long id;
    private Date start;
    private Date end;
    @NotNull
    private int duration;
    @NotNull
    private int max_attempt;
    @NotNull
    private int status;
    @NotNull
    private int type;
}
