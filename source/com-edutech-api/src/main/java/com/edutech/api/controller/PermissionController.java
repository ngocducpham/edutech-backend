package com.edutech.api.controller;

import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ErrorCode;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.permission.PermissionAdminDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.form.permission.CreatePermissionForm;
import com.edutech.api.mapper.PermissionMapper;
import com.edutech.api.storage.criteria.PermissionCriteria;
import com.edutech.api.storage.model.Permission;
import com.edutech.api.exception.RequestException;
import com.edutech.api.storage.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/permission")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class PermissionController extends ABasicController{
    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    PermissionMapper permissionMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<PermissionAdminDto>> getList(PermissionCriteria permissionCriteria, Pageable pageable){
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.PERMISSION_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<ResponseListObj<PermissionAdminDto>> apiMessageDto = new ApiMessageDto<>();
        Page<Permission> permissionPage = permissionRepository.findAll(permissionCriteria.getSpecification(),pageable);

        ResponseListObj<PermissionAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(permissionMapper.fromEntityListToAdminDtoList(permissionPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(permissionPage.getTotalPages());
        responseListObj.setTotalElements(permissionPage.getTotalElements());

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List province success");
        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreatePermissionForm createPermissionForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.PERMISSION_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Permission permission = permissionRepository.findFirstByName(createPermissionForm.getName());
        if(permission != null){
            throw new RequestException(ErrorCode.PERMISSION_ERROR_NOT_FOUND);
        }
        permission = new Permission();
        permission.setName(createPermissionForm.getName());
        permission.setAction(createPermissionForm.getAction());
        permission.setDescription(createPermissionForm.getDescription());
        permission.setShowMenu(createPermissionForm.getShowMenu());
        permission.setNameGroup(createPermissionForm.getNameGroup());
        permissionRepository.save(permission);
        apiMessageDto.setMessage("Create permission success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@PathVariable(name = "id") Long id ,@Valid @RequestBody CreatePermissionForm createPermissionForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.PERMISSION_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Permission not found"));

        permissionMapper.updatePermission(createPermissionForm, permission);
        permissionRepository.save(permission);
        apiMessageDto.setMessage("Update permission success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.PERMISSION_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Permission permission = permissionRepository.findById(id).orElse(null);
        if(permission == null){
            throw new RequestException(ErrorCode.PERMISSION_ERROR_NOT_FOUND);
        }
        permissionRepository.delete(permission);
        apiMessageDto.setMessage("Delete permission success");
        return apiMessageDto;
    }
}
