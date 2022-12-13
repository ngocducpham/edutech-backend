package com.edutech.api.mapper;

import java.util.List;

import com.edutech.api.storage.model.Group;
import com.edutech.api.dto.group.GroupAdminDto;
import com.edutech.api.dto.group.GroupDto;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "permissions", target = "permissions")
    GroupDto fromEntityToGroupDto(Group group);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "permissions", target = "permissions")
    GroupAdminDto fromEntityToGroupAdminDto(Group group);

    @IterableMapping(elementTargetType = GroupAdminDto.class)
    List<GroupAdminDto> fromEntityListToAdminDtoList(List<Group> content);

    @IterableMapping(elementTargetType = GroupDto.class)
    List<GroupDto> fromEntityListToDtoList(List<Group> content);
}
