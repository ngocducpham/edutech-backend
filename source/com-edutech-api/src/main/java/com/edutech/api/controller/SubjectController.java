package com.edutech.api.controller;


import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.subject.SubjectDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.form.subject.CreateSubjectForm;
import com.edutech.api.form.subject.UpdateSubjectForm;
import com.edutech.api.mapper.SubjectMapper;
import com.edutech.api.storage.criteria.MajorCriteria;
import com.edutech.api.storage.criteria.SubjectCriteria;
import com.edutech.api.storage.model.Subject;
import com.edutech.api.storage.repository.SubjectRepository;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/subject")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class SubjectController {
    @Autowired
    SubjectMapper subjectMapper;

    @Autowired
    SubjectRepository subjectRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<SubjectDto>> getList(SubjectCriteria criteria, Pageable pageable){
        Page<Subject> subjectPage = subjectRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListObj<SubjectDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(subjectMapper.fromEntitiesToDtos(subjectPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(subjectPage.getTotalPages());
        responseListObj.setTotalElements(subjectPage.getTotalElements());

        ApiMessageDto<ResponseListObj<SubjectDto>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List subject success");

        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<SubjectDto> get(@PathVariable("id") Long id){
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Subject not found"));

        ApiMessageDto<SubjectDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(subjectMapper.fromEntityToDto(subject));
        apiMessageDto.setMessage("Get subject success");

        return apiMessageDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<SubjectDto>> autoComplete(SubjectCriteria criteria){
        List<Subject> subjects = subjectRepository.findAll(criteria.getSpecification());

        ResponseListObj<SubjectDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(subjectMapper.fromEntitiesToDtosAutoComplete(subjects));

        ApiMessageDto<ResponseListObj<SubjectDto>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List subject success");

        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<SubjectDto> create(@Valid @RequestBody CreateSubjectForm form, BindingResult result){
        Subject subject = subjectMapper.fromCreateSubjectFormToEntity(form);
        subject = subjectRepository.save(subject);

        ApiMessageDto<SubjectDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(subjectMapper.fromEntityToDto(subject));
        apiMessageDto.setMessage("Create subject success");

        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateSubjectForm form, BindingResult result){
        Subject subject = subjectRepository.findById(form.getId())
                .orElseThrow(()-> new NotFoundException("Subject not found"));

        subjectMapper.fromUpdateSubjectFormToEntity(form, subject);
        subjectRepository.save(subject);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update subject success");

        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Subject not found"));

        subjectRepository.delete(subject);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete subject success");

        return apiMessageDto;
    }
}
