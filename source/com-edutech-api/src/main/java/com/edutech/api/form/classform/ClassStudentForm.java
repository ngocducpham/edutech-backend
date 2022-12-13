package com.edutech.api.form.classform;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ClassStudentForm {
    @NotNull
    private Long classId;

    @NotNull
    private List<Long> studentIds;
}
