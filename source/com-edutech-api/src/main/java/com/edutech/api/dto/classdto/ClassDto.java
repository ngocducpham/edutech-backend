package com.edutech.api.dto.classdto;

import com.edutech.api.dto.subject.SubjectDto;
import com.edutech.api.dto.syllabus.SyllabusDto;
import com.edutech.api.dto.teacher.TeacherDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassDto {
    private Long id;
    private String title;
    private TeacherDto teacher;
    private SubjectDto subject;
    private String avatar;
    private String description;
    private Integer status;
    private Long syllabusId;
}
