package com.edutech.api.mapper;

import com.edutech.api.dto.lesson.LessonDto;
import com.edutech.api.dto.lessondiscuss.LessonDiscussDto;
import com.edutech.api.form.lesson.CreateLessonForm;
import com.edutech.api.form.lesson.UpdateLessonForm;
import com.edutech.api.form.lessondiscuss.CreateLessonDiscussForm;
import com.edutech.api.form.lessondiscuss.UpdateLessonDiscussForm;
import com.edutech.api.storage.model.Account;
import com.edutech.api.storage.model.Lesson;
import com.edutech.api.storage.model.Class;
import com.edutech.api.storage.model.LessonDiscuss;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {LessonMapper.class, ClassMapper.class, AccountMapper.class})
public interface LessonDiscussMapper {
    @Named("lessondiscussFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "aclass.id", target = "classId")
    @Mapping(source = "account.id", target = "userId")
    @Mapping(source = "created", target = "created_date")
    @Mapping(source = "lesson.title", target = "lessonName")
    @Mapping(target = "userName", source = "account.fullName")
    @Mapping(target = "userAvatar", source = "account.avatarPath")
    LessonDiscussDto fromEntityToDto (LessonDiscuss lesson);

    @Named("lessondiscussFromEntityToDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "aclass.id", target = "classId")
    @Mapping(source = "account.id", target = "userId")
    @Mapping(source = "created", target = "created_date")
    @Mapping(source = "lesson.title", target = "lessonName")
    @Mapping(target = "userName", source = "account.fullName")
    @Mapping(target = "userAvatar", source = "account.avatarPath")
    LessonDiscussDto fromEntityToDtoAutoComplete (LessonDiscuss lesson);

    @IterableMapping(qualifiedByName = "lessondiscussFromEntityToDto")
    List<LessonDiscussDto> fromEntitiesToDtos(List<LessonDiscuss> lessonList);

    @Named("lessondiscussFromEntityToDtoAutoComplete")
    @IterableMapping(qualifiedByName = "lessondiscussFromEntityToDtoAutoComplete")
    List<LessonDiscussDto> fromEntitiesToDtosAutoComplete(List<LessonDiscuss> lessonList);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "lessonId", target = "lesson.id")
    @Mapping(source = "classId", target = "aclass.id")
    @Mapping(source = "userId", target = "account.id")
    @Mapping(source = "created_date", target = "created")
    LessonDiscuss fromCreateFormToEntity (CreateLessonDiscussForm createLessonForm);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "lessonId", target = "lesson.id")
    @Mapping(source = "classId", target = "aclass.id")
    @Mapping(source = "userId", target = "account.id")
    @Mapping(source = "status", target = "status")
    void fromUpdateFormToEntity(UpdateLessonDiscussForm updateLessonForm, @MappingTarget LessonDiscuss lesson);

}
