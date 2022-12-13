package com.edutech.api.form.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateExamForm {
    private Long id;
    private Date submit_time;
    private String attachment;
}
