package com.edutech.api.dto.teacher;

import com.edutech.api.dto.ABasicUserDto;
import lombok.Data;

@Data
public class TeacherDto extends ABasicUserDto {
    private String degree;
}
