package com.edutech.api.dto.student;

import com.edutech.api.dto.ABasicAdminDto;
import com.edutech.api.dto.ABasicUserDto;
import com.edutech.api.dto.major.MajorDto;
import com.edutech.api.dto.province.ProvinceDto;
import com.edutech.api.storage.model.Student;
import lombok.Data;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import java.util.Date;

@Data
public class StudentDto extends ABasicUserDto {
    private MajorDto major;
    private String admissionYear;
}
