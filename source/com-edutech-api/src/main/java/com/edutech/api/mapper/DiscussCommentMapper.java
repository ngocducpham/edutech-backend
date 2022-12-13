package com.edutech.api.mapper;

import com.edutech.api.dto.discusscomment.DiscussCommentDto;
import com.edutech.api.form.discusscomment.CreateDiscussCommentForm;
import com.edutech.api.form.discusscomment.UpdateDiscussCommentForm;
import com.edutech.api.storage.model.*;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
uses = {AccountMapper.class, LessonDiscussMapper.class})
public interface DiscussCommentMapper {
    @Named("discussFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "parentComment.id", target = "parentId")
    @Mapping(source = "discuss.id", target = "discussId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "userName", source = "user.fullName")
    @Mapping(target = "userAvatar", source = "user.avatarPath")
    @Mapping(source = "posted", target = "created_date")
    @Mapping(source = "childComment", target = "childs")
    DiscussCommentDto fromEntityToDto (DiscussComment discuss);

    @Named("discussFromEntityToDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "parentComment.id", target = "parentId")
    @Mapping(source = "discuss.id", target = "discussId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "userName", source = "user.fullName")
    @Mapping(target = "userAvatar", source = "user.avatarPath")
    @Mapping(source = "posted", target = "created_date")
    @Mapping(source = "childComment", target = "childs")
    DiscussCommentDto fromEntityToDtoAutoComplete (DiscussComment discuss);

    @IterableMapping(qualifiedByName = "discussFromEntityToDto")
    List<DiscussCommentDto> fromEntitiesToDtos(List<DiscussComment> discussList);

    @Named("discussFromEntityToDtoAutoComplete")
    @IterableMapping(qualifiedByName = "discussFromEntityToDtoAutoComplete")
    List<DiscussCommentDto> fromEntitiesToDtosAutoComplete(List<DiscussComment> discussList);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "content", target = "content")
    @Mapping(source = "discussId", target = "discuss.id")
    @Mapping(source = "parentId", target = "parentComment.id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "created_date", target = "posted")
    DiscussComment fromCreateFormToEntity (CreateDiscussCommentForm createcommentForm);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "content", target = "content")
    void fromUpdateFormToEntity(UpdateDiscussCommentForm updatecommentForm, @MappingTarget DiscussComment discuss);
}
