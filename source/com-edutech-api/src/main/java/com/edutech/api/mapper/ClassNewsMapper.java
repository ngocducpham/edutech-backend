package com.edutech.api.mapper;

import com.edutech.api.dto.classnews.ClassNewsDto;
import com.edutech.api.form.classnews.CreateClassNewsForm;
import com.edutech.api.form.classnews.UpdateClassNewsForm;
import com.edutech.api.storage.model.Account;
import com.edutech.api.storage.model.Class;
import com.edutech.api.storage.model.ClassNews;
import com.edutech.api.storage.model.Teacher;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {Class.class, Teacher.class, Account.class})
public interface ClassNewsMapper {
    @Named("classnewsFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "classId", source = "AClass.id")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "pintop", source = "pintop")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "imageURL", source = "imageURL")
    @Mapping(target = "teacherName", source = "news.AClass.teacher.account.fullName")
    @Mapping(target = "teacherAvatar", source = "news.AClass.teacher.account.avatarPath")
    ClassNewsDto fromEntityToDto(ClassNews news);

    @IterableMapping(qualifiedByName = "classnewsFromEntityToDto")
    List<ClassNewsDto> fromEntitiesToDtos(List<ClassNews> newsList);

    @Named("classnewsNoStatusFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "classId", source = "AClass.id")
    @Mapping(target = "pintop", source = "pintop")
    @Mapping(target = "imageURL", source = "imageURL")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "teacherName", source = "news.AClass.teacher.account.fullName")
    @Mapping(target = "teacherAvatar", source = "news.AClass.teacher.account.avatarPath")
    ClassNewsDto fromEntityNoStatusToDto(ClassNews news);

    @IterableMapping(qualifiedByName = "classnewsNoStatusFromEntityToDto")
    List<ClassNewsDto> fromEntitiesNoStatusToDtos(List<ClassNews> newsList);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "content", source = "content")
    @Mapping(target = "AClass.id", source = "classId")
    @Mapping(target = "imageURL", source = "imageURL")
    ClassNews fromCreateFormToEntity(CreateClassNewsForm form);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "content", source = "content")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "pintop", source = "pintop")
    @Mapping(target = "imageURL", source = "imageURL")
    void fromUpdateFormToEntity(UpdateClassNewsForm form, @MappingTarget ClassNews news);
}
