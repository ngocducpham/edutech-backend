package com.edutech.api.form.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateExamForm {
    private Date attempt_time;
    private String attachment;
    private Long studentId;
    private Long assignmentClassId;
}
