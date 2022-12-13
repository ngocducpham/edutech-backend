package com.edutech.api.mapper;

import com.edutech.api.dto.teacher.TeacherDto;
import com.edutech.api.dto.teacher.TeacherSubjectDto;
import com.edutech.api.storage.model.Account;
import com.edutech.api.storage.model.Teacher;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SubjectMapper.class, TeacherMapper.class})
public interface TeacherSubjectMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "account", target = "teacher", qualifiedByName = "teacherFromAccountToTeacherDtoIdName")
    @Mapping(source = "subjects", target = "subjects", qualifiedByName = "subjectFromEntitiesToDtos")
    TeacherSubjectDto fromTeacherEntityToDto(Teacher teacher);


}
