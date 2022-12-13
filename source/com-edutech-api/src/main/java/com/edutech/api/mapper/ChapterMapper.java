package com.edutech.api.mapper;

import com.edutech.api.dto.chapter.ChapterDto;
import com.edutech.api.form.chapter.CreateChapterForm;
import com.edutech.api.form.chapter.UpdateChapterForm;
import com.edutech.api.storage.model.Chapter;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SyllabusMapper.class, LessonMapper.class})
public interface ChapterMapper {

    @Named("chapterFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "syllabus.id", target = "syllabusId")
    @Mapping(source = "description", target = "description")
    ChapterDto fromEntityToDto (Chapter chapter);

    @Named("chapterFromEntityToDtoDetail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "syllabus.id", target = "syllabusId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "lessons", target = "lessons", qualifiedByName = "lessonEntitiesToDtosAutoComplete")
    ChapterDto fromEntityToDtoDetail(Chapter chapter);

    @Named("chapterEntitiesToDtosDetail")
    @IterableMapping(qualifiedByName = "chapterFromEntityToDtoDetail")
    List<ChapterDto> fromEntitiesToDtosDetail(List<Chapter> chapterList);

    @Named("chapterFromEntityToDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    ChapterDto fromEntityToDtoAutoComplete (Chapter chapter);

    @IterableMapping(qualifiedByName = "chapterFromEntityToDto")
    List<ChapterDto> fromEntitiesToDtos(List<Chapter> chapterList);

    @IterableMapping(qualifiedByName = "chapterFromEntityToDtoAutoComplete")
    List<ChapterDto> fromEntitiesToDtosAutoComplete(List<Chapter> chapterList);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "syllabusId", target = "syllabus.id")
    @Mapping(source = "description", target = "description")
    Chapter fromCreateFormToEntity (CreateChapterForm createChapterForm);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    void fromUpdateFormToEntity(UpdateChapterForm updateChapterForm, @MappingTarget Chapter chapter);
}
