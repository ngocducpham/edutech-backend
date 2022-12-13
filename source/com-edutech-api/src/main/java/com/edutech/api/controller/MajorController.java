package com.edutech.api.controller;

import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.major.MajorDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.form.major.CreateMajorForm;
import com.edutech.api.form.major.UpdateMajorForm;
import com.edutech.api.mapper.MajorMapper;
import com.edutech.api.storage.criteria.MajorCriteria;
import com.edutech.api.storage.model.Major;
import com.edutech.api.storage.repository.MajorRepository;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/major")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class MajorController {
    @Autowired
    MajorRepository majorRepository;

    @Autowired
    MajorMapper majorMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<MajorDto>> getList(MajorCriteria criteria, Pageable pageable){
        Page<Major> majorPage = majorRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListObj<MajorDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(majorMapper.fromEntitiesToDtos(majorPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(majorPage.getTotalPages());
        responseListObj.setTotalElements(majorPage.getTotalElements());

        ApiMessageDto<ResponseListObj<MajorDto>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List major success");

        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<MajorDto> get(@PathVariable("id") Long id){
        Major major = majorRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Major not found"));

        ApiMessageDto<MajorDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(majorMapper.fromEntityToDto(major));
        apiMessageDto.setMessage("Get major success");

        return apiMessageDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<MajorDto>> autoComplete(MajorCriteria criteria){
        List<Major> majors = majorRepository.findAll(criteria.getSpecification());

        ResponseListObj<MajorDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(majorMapper.fromEntitiesToDtosIdName(majors));

        ApiMessageDto<ResponseListObj<MajorDto>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("Auto complete major success");

        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<MajorDto> create(@Valid @RequestBody CreateMajorForm form, BindingResult result){
        Major major = majorMapper.fromCreateMajorFormToEntity(form);
        major = majorRepository.save(major);

        ApiMessageDto<MajorDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(majorMapper.fromEntityToDto(major));
        apiMessageDto.setMessage("Create major success");

        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateMajorForm form, BindingResult result){
        Major major = majorRepository.findById(form.getId())
                .orElseThrow(()-> new NotFoundException("Major not found"));

        majorMapper.fromUpdateMajorFormToEntity(form, major);
        major = majorRepository.save(major);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update major success");

        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        Major major = majorRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Major not found"));

        majorRepository.delete(major);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete major success");

        return apiMessageDto;
    }
}
