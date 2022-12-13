package com.edutech.api.form.teacher;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TeacherSubjectForm {
    @NotNull
    private Long teacherId;
    @NotNull
    private List<Long> subjectIds;
}
