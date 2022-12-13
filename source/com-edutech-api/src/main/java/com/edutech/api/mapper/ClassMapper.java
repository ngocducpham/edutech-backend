package com.edutech.api.mapper;

import com.edutech.api.dto.classdto.ClassDto;
import com.edutech.api.form.classform.CreateClassForm;
import com.edutech.api.form.classform.UpdateClassForm;
import com.edutech.api.storage.model.Class;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {TeacherMapper.class, SubjectMapper.class})
public interface ClassMapper {

    @Named("classFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "teacher", source = "teacher", qualifiedByName = "teacherFromEntityToDtoAutoComplete")
    @Mapping(target = "subject", source = "subject", qualifiedByName = "subjectFromEntityToDtoAutoComplete")
    @Mapping(target = "avatar", source = "avatar")
    @Mapping(target = "syllabusId", source = "syllabus.id")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "status", source = "status")
    ClassDto fromEntityToDto(Class aClass);

    @IterableMapping(qualifiedByName = "classFromEntityToDto")
    List<ClassDto> fromEntitiesToDtos(List<Class> classes);

    @Named("classNoStatusFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "teacher", source = "teacher", qualifiedByName = "teacherFromEntityToDtoAutoComplete")
    @Mapping(target = "subject", source = "subject", qualifiedByName = "subjectFromEntityToDtoAutoComplete")
    @Mapping(target = "syllabusId", source = "syllabus.id")
    @Mapping(target = "avatar", source = "avatar")
    @Mapping(target = "description", source = "description")
    ClassDto fromEntityNoStatusToDto(Class aClass);

    @IterableMapping(qualifiedByName = "classNoStatusFromEntityToDto")
    List<ClassDto> fromEntitiesNoStatusToDtos(List<Class> classes);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "teacher.id", source = "teacherId")
    @Mapping(target = "subject.id", source = "subjectId")
    @Mapping(target = "avatar", source = "avatar")
    @Mapping(target = "description", source = "description")
    Class fromCreateFormToEntity(CreateClassForm form);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "teacher.id", source = "teacherId")
    @Mapping(target = "subject.id", source = "subjectId")
    @Mapping(target = "syllabus.id", source = "syllabusId")
    @Mapping(target = "avatar", source = "avatar")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "status", source = "status")
    void fromUpdateFormToEntity(UpdateClassForm form, @MappingTarget Class aClass);

   @AfterMapping
   default void setFieldsNullWhenIdIsNull(@MappingTarget Class cls) {
       if (cls.getTeacher() != null && cls.getTeacher().getId() == null) {
           cls.setTeacher(null);
       }
       if (cls.getSubject() != null && cls.getSubject().getId() == null) {
           cls.setSubject(null);
       }
       if (cls.getSyllabus() != null && cls.getSyllabus().getId() == null) {
           cls.setSyllabus(null);
       }
   }
}
