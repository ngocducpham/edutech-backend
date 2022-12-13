package com.edutech.api.mapper;

import com.edutech.api.dto.assignment.AssignmentDto;

import com.edutech.api.form.assignment.CreateAssignmentForm;
import com.edutech.api.form.assignment.UpdateAssignmentForm;
import com.edutech.api.storage.model.Assignment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {LessonMapper.class})
public interface AssignmentMapper {

    @Named("assignmentFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "lesson", source = "lesson", qualifiedByName = "lessonFromEntityToDtoAutoComplete")
    AssignmentDto fromEntityToDto(Assignment assignment);

    @IterableMapping(qualifiedByName = "assignmentFromEntityToDto")
    List<AssignmentDto> fromEntitiesToDtos(List<Assignment> assignments);

    @Named("assignmentFromEntityToDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "type", source = "type")
    AssignmentDto fromEntityToDtoAutoComplete(Assignment assignment);

    @IterableMapping(qualifiedByName = "assignmentFromEntityToDtoAutoComplete")
    List<AssignmentDto> fromEntitiesToDtosAutoComplete(List<Assignment> assignments);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "lesson.id", source = "lessonId")
    Assignment fromCreateFormToEntity(CreateAssignmentForm form);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "status", source = "status")
    void fromUpdateFormToEntity(UpdateAssignmentForm form, @MappingTarget Assignment assignment);
}
