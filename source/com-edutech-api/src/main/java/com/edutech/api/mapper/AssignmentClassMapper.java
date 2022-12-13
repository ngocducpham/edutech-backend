package com.edutech.api.mapper;

import com.edutech.api.dto.assignmentclass.AssignmentClassDto;
import com.edutech.api.form.assignmentclass.CreateAssignmentClassForm;
import com.edutech.api.form.assignmentclass.UpdateAssignmentClassForm;
import com.edutech.api.storage.model.AssignmentClass;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ClassMapper.class, AssignmentMapper.class})
public interface AssignmentClassMapper {
    @Named("assignmentclassFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "start", source = "start")
    @Mapping(target = "end", source = "end")
    @Mapping(target = "duration", source = "duration")
    @Mapping(target = "maxAttempt", source = "max_attempt")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "classId", source = "AClass.id")
    @Mapping(target = "className", source = "AClass.title")
    @Mapping(target = "assignmentId", source = "assignment.id")
    @Mapping(target = "assignmentTitle", source = "assignment.title")
    @Mapping(target = "assignmentDescription", source = "assignment.description")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "title", source = "assignment.title")
    @Mapping(target = "lessonId", source = "assignment.lesson.id")
    @Mapping(target = "description", source = "assignment.description")
    AssignmentClassDto fromEntityToDto(AssignmentClass assignmentclass);

    @IterableMapping(qualifiedByName = "assignmentclassFromEntityToDto")
    List<AssignmentClassDto> fromEntitiesToDtos(List<AssignmentClass> assignmentclasses);

    @Named("assignmentclassFromEntityToDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssignmentClassDto fromEntityToDtoAutoComplete(AssignmentClass assignmentclass);

    @IterableMapping(qualifiedByName = "assignmentclassFromEntityToDtoAutoComplete")
    List<AssignmentClassDto> fromEntitiesToDtosAutoComplete(List<AssignmentClass> assignmentclasses);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "AClass.id", source = "classId")
    @Mapping(target = "assignment.id", source = "assignmentId")
    @Mapping(target = "duration", source = "duration")
    @Mapping(target = "max_attempt", source = "max_attempt")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "type", source = "type")
    AssignmentClass fromCreateFormToEntity(CreateAssignmentClassForm form);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "start", source = "start")
    @Mapping(target = "end", source = "end")
    @Mapping(target = "duration", source = "duration")
    @Mapping(target = "max_attempt", source = "max_attempt")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "type", source = "type")
    void fromUpdateFormToEntity(UpdateAssignmentClassForm form, @MappingTarget AssignmentClass assignmentclass);
}
