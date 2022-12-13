package com.edutech.api.controller;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ErrorCode;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.province.ProvinceDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.exception.RequestException;
import com.edutech.api.form.province.CreateProvinceForm;
import com.edutech.api.form.province.UpdateProvinceForm;
import com.edutech.api.mapper.ProvinceMapper;
import com.edutech.api.service.EduTechApiService;
import com.edutech.api.storage.criteria.CategoryCriteria;
import com.edutech.api.storage.criteria.ProvinceCriteria;
import com.edutech.api.storage.model.Account;
import com.edutech.api.storage.model.Category;
import com.edutech.api.storage.model.News;
import com.edutech.api.storage.model.Province;
import com.edutech.api.storage.repository.ProvinceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/v1/province")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ProvinceController extends ABasicController{
    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    ProvinceMapper provinceMapper;

    @Autowired
    EduTechApiService eduTechApiService;
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProvinceDto>> list(@Valid ProvinceCriteria provinceCriteria, Pageable pageable, BindingResult result) {
        ApiMessageDto<ResponseListObj<ProvinceDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Province> listProvince = provinceRepository.findAll(provinceCriteria.getSpecification(), pageable);
        ResponseListObj<ProvinceDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(provinceMapper.fromEntityListToProvinceDtoList(listProvince.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listProvince.getTotalPages());
        responseListObj.setTotalElements(listProvince.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }


    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProvinceForm createProvinceForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        if(createProvinceForm.getParentProvinceId() != null) {
            Province parentProvince = provinceRepository.findById(createProvinceForm.getParentProvinceId())
                    .orElseThrow(() -> new NotFoundException("Province not found"));

            if(parentProvince.getKind().equals(EduTechConstant.PROVINCE_KIND_COMMUNE)) {
                throw new RequestException("Province parent is not valid");
            }

            String parentProvinceKind = parentProvince.getKind();
            if(parentProvinceKind.equals(EduTechConstant.PROVINCE_KIND_PROVINCE)) {
                createProvinceForm.setKind(EduTechConstant.PROVINCE_KIND_DISTRICT);
            } else if(parentProvinceKind.equals(EduTechConstant.PROVINCE_KIND_DISTRICT)) {
                createProvinceForm.setKind(EduTechConstant.PROVINCE_KIND_COMMUNE);
            }else {
                throw new RequestException("Province parent is not valid");
            }
        }

        Province province = provinceMapper.fromCreateProvinceFormToEntity(createProvinceForm);
        provinceRepository.save(province);
        apiMessageDto.setMessage("Create province success");
        return apiMessageDto;

    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProvinceForm updateProvinceForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Province province = provinceRepository.findById(updateProvinceForm.getProvinceId()).orElse(null);
        if (province == null) {
            throw new NotFoundException("Province not found");
        }

        provinceMapper.fromUpdateProvinceFormToEntity(updateProvinceForm, province);

        provinceRepository.save(province);

        apiMessageDto.setMessage("Update province success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProvinceDto> get(@PathVariable("id") Long id){

        ApiMessageDto<ProvinceDto> result = new ApiMessageDto<>();
        Province province = provinceRepository.findById(id).orElse(null);
        if(province == null){
            throw new NotFoundException("Province not found");
        }

        result.setData(provinceMapper.fromEntityToProvinceDto(province));
        result.setMessage("Get province success");
        return result;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        ApiMessageDto<String> result = new ApiMessageDto<>();
        Province province = provinceRepository.findById(id).orElse(null);
        if(province == null){
            throw new NotFoundException("Province not found");
        }

        provinceRepository.delete(province);
        result.setMessage("Delete province success");
        return result;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProvinceDto>> autoComplete(ProvinceCriteria provinceCriteria) {
        ApiMessageDto<ResponseListObj<ProvinceDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        Page<Province> listProvince = provinceRepository.findAll(provinceCriteria.getSpecification(), Pageable.unpaged());
        ResponseListObj<ProvinceDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(provinceMapper.fromEntityListToProvinceDtoAutoComplete(listProvince.getContent()));

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }
}
