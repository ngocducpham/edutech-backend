package com.edutech.api.dto.teacher;

import com.edutech.api.dto.subject.SubjectDto;
import lombok.Data;

import java.util.List;

@Data
public class TeacherSubjectDto {
    private TeacherDto teacher;
    private List<SubjectDto> subjects;
}
