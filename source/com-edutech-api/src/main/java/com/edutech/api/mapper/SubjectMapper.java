package com.edutech.api.mapper;

import com.edutech.api.dto.subject.SubjectDto;
import com.edutech.api.form.subject.CreateSubjectForm;
import com.edutech.api.form.subject.UpdateSubjectForm;
import com.edutech.api.storage.model.Subject;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SubjectMapper {
    @Named("subjectFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "status", target = "status")
    SubjectDto fromEntityToDto(Subject subject);

    @Named("subjectFromEntitiesToDtos")
    @IterableMapping(qualifiedByName = "subjectFromEntityToDto")
    List<SubjectDto> fromEntitiesToDtos(List<Subject> subjects);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "code", target = "code")
    Subject fromCreateSubjectFormToEntity(CreateSubjectForm subjectDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "status", target = "status")
    void fromUpdateSubjectFormToEntity(UpdateSubjectForm form, @MappingTarget Subject subject);

    @Named("subjectFromEntityToDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    SubjectDto fromEntityToDtoAutoComplete(Subject subject);

    @Named("subjectFromEntitiesToDtosAutoComplete")
    @IterableMapping(qualifiedByName = "subjectFromEntityToDtoAutoComplete")
    List<SubjectDto> fromEntitiesToDtosAutoComplete(List<Subject> subjects);
}
