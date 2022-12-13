package com.edutech.api.mapper;

import com.edutech.api.dto.lesson.LessonDto;
import com.edutech.api.form.lesson.CreateLessonForm;
import com.edutech.api.form.lesson.UpdateLessonForm;
import com.edutech.api.storage.model.Assignment;
import com.edutech.api.storage.model.Chapter;
import com.edutech.api.storage.model.Lesson;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {Chapter.class})
public interface LessonMapper {

    @Named("lessonFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "chapter.id", target = "chapterId")
    @Mapping(source = "chapter.title", target = "chapterName")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "order", target = "order")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "attachment", target = "attachment")
    @Mapping(source = "icon", target = "icon")
    LessonDto fromEntityToDto (Lesson lesson);

    @Named("lessonFromEntityToDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "chapter.id", target = "chapterId")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "order", target = "order")
    @Mapping(source = "icon", target = "icon")
    @Mapping(source = "assignments", target = "hasAssignment", qualifiedByName = "checkHasAssignment")
    LessonDto fromEntityToDtoAutoComplete (Lesson lesson);

    @IterableMapping(qualifiedByName = "lessonFromEntityToDto")
    List<LessonDto> fromEntitiesToDtos(List<Lesson> lessonList);

    @Named("lessonEntitiesToDtosAutoComplete")
    @IterableMapping(qualifiedByName = "lessonFromEntityToDtoAutoComplete")
    List<LessonDto> fromEntitiesToDtosAutoComplete(List<Lesson> lessonList);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "chapterId", target = "chapter.id")
    @Mapping(source = "order", target = "order")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "attachment", target = "attachment")
    @Mapping(source = "icon", target = "icon")
    Lesson fromCreateFormToEntity (CreateLessonForm createLessonForm);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "order", target = "order")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "attachment", target = "attachment", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(source = "icon", target = "icon")
    void fromUpdateFormToEntity(UpdateLessonForm updateLessonForm, @MappingTarget Lesson lesson);

    @Named("checkHasAssignment")
    default Boolean checkHaveAssignment(List<Assignment> assignments) {
        return assignments != null && !assignments.isEmpty();
    }
}
