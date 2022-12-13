package com.edutech.api.mapper;

import com.edutech.api.dto.student.StudentDto;
import com.edutech.api.dto.teacher.TeacherDto;
import com.edutech.api.form.student.CreateStudentForm;
import com.edutech.api.form.student.UpdateStudentForm;
import com.edutech.api.form.student.UpdateStudentProfileForm;
import com.edutech.api.storage.model.Account;
import com.edutech.api.storage.model.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ProvinceMapper.class, CommonMapper.class, MajorMapper.class})
public interface StudentMapper {
    @Named("studentFromEntityToDto")
    @Mapping (source = "major", target = "major", qualifiedByName = "majorFromEntityToDtoIdName")
    @Mapping (source = "admissionYear", target = "admissionYear")
    @Mapping (source = "address", target = "address")
    @Mapping(source = "account.avatarPath", target = "avatarPath")
    @Mapping (source = "province", target = "province", qualifiedByName = "adminProvinceAutoCompleteMapping")
    @Mapping (source = "district", target = "district", qualifiedByName = "adminProvinceAutoCompleteMapping")
    @Mapping (source = "commune", target = "commune", qualifiedByName = "adminProvinceAutoCompleteMapping")
    @Mapping(source = "account", target = ".")
    StudentDto fromEntityToDto (Student student);
    @Named("studentFromEntitiesToDtos")
    @IterableMapping(qualifiedByName = "studentFromEntityToDto")
    List<StudentDto> fromEntitiesToDtos(List<Student> students);

    @BeanMapping(ignoreByDefault = true)
    @Mapping (source = "majorId", target = "major.id")
    @Mapping (source = "admissionYear", target = "admissionYear")
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
    Student fromCreateStudentFormToEntity (CreateStudentForm createStudentForm);

    @BeanMapping(ignoreByDefault = true, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    @Mapping (source = "majorId", target = "major.id")
    @Mapping (source = "admissionYear", target = "admissionYear")
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
    void fromUpdateStudentFormToEntity (UpdateStudentForm updateStudentForm, @MappingTarget Student student);

    @BeanMapping(ignoreByDefault = true)
    @Mapping (source = "address", target = "address")
    @Mapping (source = "province", target = "province", qualifiedByName = "adminProvinceAutoCompleteMapping")
    @Mapping (source = "district", target = "district", qualifiedByName = "adminProvinceAutoCompleteMapping")
    @Mapping (source = "commune", target = "commune", qualifiedByName = "adminProvinceAutoCompleteMapping")
    @Mapping (source = "major", target = "major", qualifiedByName = "majorFromEntityToDtoIdName")
    @Mapping (source = "admissionYear", target = "admissionYear")
    @Mapping(source = "account.id", target = "id")
    @Mapping(source = "account.fullName", target = "fullName")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.phone", target = "phone")
    @Mapping(source = "account.birthDay", target = "birthDay")
    @Mapping(source = "account.avatarPath", target = "avatarPath")
    @Mapping(source = "account.kind", target = "kind")
    StudentDto fromEntityToDtoProfile (Student student);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "password", target = "account.password", qualifiedByName = "encodePassword")
    void fromUpdateStudentProfileFormToEntity (UpdateStudentProfileForm form, @MappingTarget Student student);

    @Named("studentFromAccountToTeacherDtoIdName")
    default StudentDto fromAccountToStudentIdNameDto(Account account){
        if (account == null) {
            return null;
        }
        StudentDto studentDto = new StudentDto();
        studentDto.setId(account.getId());
        studentDto.setFullName(account.getFullName());
        return studentDto;
    }

    @AfterMapping
    default void setProvinceNullWhenProvinceIdNull(@MappingTarget Student student) {
        if (student.getProvince() != null && student.getProvince().getProvinceId() == null) {
            student.setProvince(null);
            student.setDistrict(null);
            student.setCommune(null);
            return;
        }
        if (student.getCommune() != null && student.getCommune().getProvinceId() == null) {
            student.setCommune(null);
        }
        if (student.getDistrict() != null && student.getDistrict().getProvinceId() == null) {
            student.setDistrict(null);
            student.setCommune(null);
        }
    }
}
