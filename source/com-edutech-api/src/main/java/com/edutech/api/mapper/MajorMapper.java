package com.edutech.api.mapper;

import com.edutech.api.dto.major.MajorDto;
import com.edutech.api.form.major.CreateMajorForm;
import com.edutech.api.form.major.UpdateMajorForm;
import com.edutech.api.storage.model.Major;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MajorMapper {
    @Named("majorFromEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "status", target = "status")
    MajorDto fromEntityToDto(Major major);


    @IterableMapping(qualifiedByName = "majorFromEntityToDto")
    List<MajorDto> fromEntitiesToDtos(List<Major> majors);

    @Named("majorFromEntityToDtoIdName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    MajorDto fromEntityToDtoIdName(Major major);

    @IterableMapping(qualifiedByName = "majorFromEntityToDtoIdName")
    List<MajorDto> fromEntitiesToDtosIdName(List<Major> majors);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "image", target = "image")
    Major fromCreateMajorFormToEntity(CreateMajorForm createMajorForm);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "status", target = "status")
    void fromUpdateMajorFormToEntity(UpdateMajorForm form, @MappingTarget Major major);
}