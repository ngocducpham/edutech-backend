package com.edutech.api.mapper;

import com.edutech.api.dto.classdto.ClassStudentDto;
import com.edutech.api.storage.model.Class;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ClassMapper.class, StudentMapper.class})
public interface ClassStudentMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "cls", target = "classDto", qualifiedByName = "classFromEntityToDto")
    @Mapping(source = "cls.students", target = "students", qualifiedByName = "studentFromEntitiesToDtos")
    ClassStudentDto fromClassEntityToDto(Class cls);
}
