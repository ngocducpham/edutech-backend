package com.edutech.api.controller;

import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.syllabus.SyllabusDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.exception.RequestException;
import com.edutech.api.form.syllabus.SyllabusForm;
import com.edutech.api.mapper.SyllabusMapper;
import com.edutech.api.storage.criteria.SyllabusCriteria;
import com.edutech.api.storage.model.Syllabus;
import com.edutech.api.storage.repository.SubjectRepository;
import com.edutech.api.storage.repository.SyllabusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/v1/syllabus")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class SyllabusController extends ABasicController {
    @Autowired
    SyllabusRepository syllabusRepository;

    @Autowired
    SyllabusMapper syllabusMapper;

    @Autowired
    SubjectRepository subjectRepository;


    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<SyllabusDto>> getList(SyllabusCriteria criteria, Pageable pageable) {
        criteria.setTeacherId(getCurrentUserId());

        return getSyllabusesMessageDto(criteria, pageable, syllabusMapper::fromEntitiesToDtos);
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<SyllabusDto>> autoComplete(SyllabusCriteria criteria) {
        criteria.setTeacherId(getCurrentUserId());

        return getSyllabusesMessageDto(criteria, Pageable.unpaged(), syllabusMapper::fromEntitiesToDtosAutoComplete);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<SyllabusDto> get(@PathVariable Long id) {
        Syllabus syllabus = syllabusRepository.findByIdAndTeacherId(id, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));

        ApiMessageDto<SyllabusDto> response = new ApiMessageDto<>();
        response.setData(syllabusMapper.fromEntityToDtoDetail(syllabus));
        response.setMessage("Get syllabus successfully");

        return response;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody SyllabusForm form, BindingResult result) {
        subjectRepository.findByIdAndTeacherId(form.getSubjectId(), getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Subject not found"));

        Syllabus syllabus = syllabusMapper.fromCreateFormToEntity(form, getCurrentUserId());

        syllabusRepository.save(syllabus);


        ApiMessageDto<String> response = new ApiMessageDto<>();
        response.setMessage("Create syllabus successfully");

        return response;
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@PathVariable Long id, @Valid @RequestBody SyllabusForm form, BindingResult result) {
        Syllabus syllabus = syllabusRepository.findByIdAndTeacherId(id, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));

        subjectRepository.findByIdAndTeacherId(form.getSubjectId(), getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Subject not found"));

        syllabusMapper.fromUpdateFormToEntity(form, syllabus);

        syllabusRepository.save(syllabus);

        ApiMessageDto<String> response = new ApiMessageDto<>();
        response.setMessage("Update syllabus successfully");

        return response;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable Long id) {
        Syllabus syllabus = syllabusRepository.findByIdAndTeacherId(id, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));


        syllabusRepository.delete(syllabus);

        ApiMessageDto<String> response = new ApiMessageDto<>();
        response.setMessage("Delete syllabus successfully");

        return response;
    }

    private ApiMessageDto<ResponseListObj<SyllabusDto>> getSyllabusesMessageDto(
            SyllabusCriteria criteria, Pageable pageable, Function<List<Syllabus>, List<SyllabusDto>> mapper) {
        Page<Syllabus> syllabusPage = syllabusRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListObj<SyllabusDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(mapper.apply(syllabusPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(syllabusPage.getTotalPages());
        responseListObj.setTotalElements(syllabusPage.getTotalElements());

        ApiMessageDto<ResponseListObj<SyllabusDto>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("Get list syllabus successfully");

        return apiMessageDto;
    }
}
