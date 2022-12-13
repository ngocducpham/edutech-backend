package com.edutech.api.dto.classdto;

import com.edutech.api.dto.student.StudentDto;
import lombok.Data;

import java.util.List;

@Data
public class ClassStudentDto {
    private ClassDto classDto;
    private List<StudentDto> students;
}
