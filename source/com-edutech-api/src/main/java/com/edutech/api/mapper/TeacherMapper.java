package com.edutech.api.mapper;

import com.edutech.api.dto.teacher.TeacherDto;
import com.edutech.api.form.teacher.CreateTeacherForm;
import com.edutech.api.form.teacher.UpdateTeacherForm;
import com.edutech.api.form.teacher.UpdateTeacherProfileForm;
import com.edutech.api.storage.model.Account;
import com.edutech.api.storage.model.Teacher;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ProvinceMapper.class, CommonMapper.class})
public interface TeacherMapper {
    @Named("teacherFromEntityToDto")
    //@BeanMapping(ignoreByDefault = true)
    @Mapping (source = "degree", target = "degree")
    @Mapping (source = "address", target = "address")
    @Mapping(source = "account.avatarPath", target = "avatarPath")
    @Mapping (source = "province", target = "province", qualifiedByName = "adminProvinceAutoCompleteMapping")
    @Mapping (source = "district", target = "district", qualifiedByName = "adminProvinceAutoCompleteMapping")
    @Mapping (source = "commune", target = "commune", qualifiedByName = "adminProvinceAutoCompleteMapping")
    @Mapping(source = "account", target = ".")
    TeacherDto fromEntityToDto (Teacher teacher);

    //@BeanMapping(ignoreByDefault = true)
    @IterableMapping(qualifiedByName = "teacherFromEntityToDto")
    List<TeacherDto> fromEntitiesToDtos(List<Teacher> teachers);

    @BeanMapping(ignoreByDefault = true)
    @Mapping (source = "degree", target = "degree")
    @Mapping (source = "address", target = "address")
    @Mapping (source = "username", target = "account.username")
    @Mapping (source = "fullName", target = "account.fullName")
    @Mapping (source = "email", target = "account.email")
    @Mapping (source = "phone", target = "account.phone")
    @Mapping (source = "birthDay", target = "account.birthDay")
    @Mapping (source = "provinceId", target = "province.provinceId")
    @Mapping (source = "districtId", target = "district.provinceId")
    @Mapping (source = "communeId", target = "commune.provinceId")
    @Mapping (source = "avatarPath", target = "account.avatarPath")
    @Mapping(source = "password", target = "account.password", qualifiedByName = "encodePassword")
    Teacher fromCreateTeacherFormToEntity(CreateTeacherForm createTeacherForm);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "password", target = "account.password", qualifiedByName = "encodePassword")
    Teacher fromUpdateTeacherProfileFormToEntity(UpdateTeacherProfileForm form, @MappingTarget Teacher teacher);

    @BeanMapping(ignoreByDefault = true, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    @Mapping (source = "degree", target = "degree")
    @Mapping (source = "address", target = "address")
    @Mapping (source = "provinceId", target = "province.provinceId")
    @Mapping (source = "districtId", target = "district.provinceId")
    @Mapping (source = "communeId", target = "commune.provinceId")
    @Mapping (source = "email", target = "account.email")
    @Mapping (source = "phone", target = "account.phone")
    @Mapping (source = "birthDay", target = "account.birthDay")
    @Mapping (source = "avatarPath", target = "account.avatarPath")
    @Mapping (source = "fullName", target = "account.fullName")
    @Mapping(source = "status", target = "account.status")
    @Mapping(source = "password", target = "account.password", qualifiedByName = "encodePassword")
    void fromUpdateTeacherFormToEntity(UpdateTeacherForm form, @MappingTarget Teacher teacher);

    @Named("teacherFromAccountToTeacherDtoIdName")
    default TeacherDto fromAccountToTeacherIdNameDto(Account account){
        if (account == null) {
            return null;
        }
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(account.getId());
        teacherDto.setFullName(account.getFullName());
        return teacherDto;
    }

    @Mapping (source = "degree", target = "degree")
    @Mapping (source = "address", target = "address")
    @Mapping (source = "province", target = "province", qualifiedByName = "adminProvinceAutoCompleteMapping")
    @Mapping (source = "district", target = "district", qualifiedByName = "adminProvinceAutoCompleteMapping")
    @Mapping (source = "commune", target = "commune", qualifiedByName = "adminProvinceAutoCompleteMapping")
    @Mapping(source = "account.id", target = "id")
    @Mapping(source = "account.fullName", target = "fullName")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.phone", target = "phone")
    @Mapping(source = "account.birthDay", target = "birthDay")
    @Mapping(source = "account.avatarPath", target = "avatarPath")
    @Mapping(source = "account.kind", target = "kind")
    TeacherDto fromEntityToDtoProfile(Teacher teacher);

    @Named("teacherFromEntityToDtoAutoComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "account.id", target = "id")
    @Mapping(source = "account.fullName", target = "fullName")
    @Mapping(source = "account.avatarPath", target = "avatarPath")
    TeacherDto fromEntityToDtoAutoComplete(Teacher teacher);

    @Named("teacherFromEntitiesToDtosAutoComplete")
    @IterableMapping(qualifiedByName = "teacherFromEntityToDtoAutoComplete")
    List<TeacherDto> fromEntitiesToDtosAutoComplete(List<Teacher> teachers);

    @AfterMapping
    default void setProvinceNullWhenProvinceIdNull(@MappingTarget Teacher teacher) {
        if (teacher.getProvince() != null && teacher.getProvince().getProvinceId() == null) {
            teacher.setProvince(null);
            teacher.setDistrict(null);
            teacher.setCommune(null);
            return;
        }
        if (teacher.getCommune() != null && teacher.getCommune().getProvinceId() == null) {
            teacher.setCommune(null);
        }
        if (teacher.getDistrict() != null && teacher.getDistrict().getProvinceId() == null) {
            teacher.setDistrict(null);
            teacher.setCommune(null);
        }
    }
}
