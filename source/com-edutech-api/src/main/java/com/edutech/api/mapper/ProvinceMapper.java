package com.edutech.api.mapper;

import com.edutech.api.dto.province.ProvinceDto;
import com.edutech.api.form.province.CreateProvinceForm;
import com.edutech.api.form.province.UpdateProvinceForm;
import com.edutech.api.storage.model.Province;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProvinceMapper {

    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateProvinceFormToEntity")
    @Mapping(source = "provinceName", target = "provinceName")
    @Mapping(source = "parentProvinceId", target = "parentProvince.provinceId")
    @Mapping(source = "kind", target = "kind")
    Province fromCreateProvinceFormToEntity (CreateProvinceForm createProvinceForm);

    @Mapping(source = "provinceName", target = "provinceName")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateProvinceFormToEntity(UpdateProvinceForm updateProvinceForm, @MappingTarget Province province);

    @Mapping(source = "kind", target = "provinceKind")
    @Mapping(source = "parentProvince.provinceId", target = "parentProvinceId" )
    ProvinceDto fromEntityToProvinceDto(Province province);

    @IterableMapping(elementTargetType = ProvinceDto.class)
    List<ProvinceDto> fromEntityListToProvinceDtoList(List<Province> provinces);

    @Mapping(source = "provinceId", target = "provinceId")
    @Mapping(source = "provinceName", target = "provinceName")
    @Mapping(source = "kind", target = "provinceKind")
    @Mapping(source = "parentProvince.provinceId", target = "parentProvinceId" )
    @BeanMapping(ignoreByDefault = true)
    @Named("adminProvinceAutoCompleteMapping")
    ProvinceDto fromEntityToAdminDtoAutoComplete(Province province);

    @IterableMapping(elementTargetType = ProvinceDto.class, qualifiedByName = "adminProvinceAutoCompleteMapping")
    List<ProvinceDto> fromEntityListToProvinceDtoAutoComplete(List<Province> provinces);

    @AfterMapping
    default Province setParentProvinceNullWhenProvinceParentIdIsNull(@MappingTarget Province province) {
        if (province.getParentProvince() != null && province.getParentProvince().getProvinceId() == null) {
            province.setParentProvince(null);
        }
        return province;
    }
}
