package com.edutech.api.mapper;

import com.edutech.api.dto.syllabus.SyllabusDto;
import com.edutech.api.form.syllabus.SyllabusForm;
import com.edutech.api.storage.model.Syllabus;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SubjectMapper.class, ChapterMapper.class})
public interface SyllabusMapper {

    @Named("syllabusFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "subject", target = "subject", qualifiedByName = "subjectFromEntityToDtoAutoComplete")
    SyllabusDto fromEntityToDto (Syllabus syllabus);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "subject", target = "subject", qualifiedByName = "subjectFromEntityToDtoAutoComplete")
    @Mapping(source = "chapters", target = "chapters", qualifiedByName = "chapterEntitiesToDtosDetail")
    SyllabusDto fromEntityToDtoDetail (Syllabus syllabus);

    @Named("syllabusFromEntityToDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    SyllabusDto fromEntityToDtoAutoComplete (Syllabus syllabus);

    @IterableMapping(qualifiedByName = "syllabusFromEntityToDto")
    List<SyllabusDto> fromEntitiesToDtos(List<Syllabus> syllabuses);

    @IterableMapping(qualifiedByName = "syllabusFromEntityToDtoAutoComplete")
    List<SyllabusDto> fromEntitiesToDtosAutoComplete(List<Syllabus> syllabuses);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "form.title", target = "title")
    @Mapping(source = "form.description", target = "description")
    @Mapping(source = "form.avatar", target = "avatar")
    @Mapping(source = "form.subjectId", target = "subject.id")
    @Mapping(source = "teacherId", target = "teacher.id")
    Syllabus fromCreateFormToEntity(SyllabusForm form, Long teacherId);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "subjectId", target = "subject.id")
    void fromUpdateFormToEntity(SyllabusForm form, @MappingTarget Syllabus syllabus);
}
