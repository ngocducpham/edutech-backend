package com.edutech.api.mapper;

import com.edutech.api.storage.model.Account;
import com.edutech.api.dto.account.AccountAdminDto;
import com.edutech.api.dto.account.AccountDto;
import com.edutech.api.form.account.CreateAccountAdminForm;
import com.edutech.api.form.account.UpdateAccountAdminForm;
import com.edutech.api.form.account.UpdateProfileAdminForm;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "birthDay", target = "birthDay")
    Account fromCreateAccountAdminFormToAdmin(CreateAccountAdminForm createAccountAdminForm);


    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "phone", target = "phone")
    @BeanMapping(ignoreByDefault = true)
    void mappingFormUpdateAdminToEntity(UpdateAccountAdminForm updateAccountAdminForm,@MappingTarget Account account);

    @Mapping(source = "fullName", target = "fullName")
    void mappingFormUpdateProfileToEntity(UpdateProfileAdminForm updateProfileAdminForm, @MappingTarget Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "group", target = "group")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "avatarPath", target = "avatar")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "isSuperAdmin", target = "isSuperAdmin")
    @Mapping(source = "birthDay", target = "birthDay")
    AccountDto fromEntityToAccountDto(Account account);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "group", target = "group")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "avatarPath", target = "avatar")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "birthDay", target = "birthDay")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    AccountAdminDto fromEntityToAccountAdminDto(Account account);

    @IterableMapping(elementTargetType = AccountAdminDto.class, qualifiedByName = "adminGetMapping")
    List<AccountAdminDto> fromEntityListToDtoList(List<Account> content);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "fullName", target = "fullName")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminAutoCompleteMapping")
    AccountAdminDto fromEntityToAccountAdminDtoAutoComplete(Account account);
}
