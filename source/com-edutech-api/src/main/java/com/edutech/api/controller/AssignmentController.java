package com.edutech.api.controller;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.assignment.AssignmentDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.form.assignment.CreateAssignmentForm;
import com.edutech.api.form.assignment.UpdateAssignmentForm;
import com.edutech.api.mapper.AssignmentMapper;
import com.edutech.api.storage.criteria.AssignmentCriteria;
import com.edutech.api.storage.model.Assignment;
import com.edutech.api.storage.repository.AssignmentRepository;
import com.edutech.api.storage.repository.LessonRepository;
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
@RequestMapping("/v1/assignment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AssignmentController extends ABasicController{

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<AssignmentDto>> getList(AssignmentCriteria criteria, Pageable pageable){
        criteria.setTeacherId(getCurrentUserId());

        return getAssignmentsMessageDto(criteria, pageable, assignmentMapper::fromEntitiesToDtos);
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<AssignmentDto>> autoComplete(AssignmentCriteria criteria){
        criteria.setTeacherId(getCurrentUserId());
        criteria.setStatus(EduTechConstant.STATUS_ACTIVE);

        return getAssignmentsMessageDto(criteria, Pageable.unpaged(), assignmentMapper::fromEntitiesToDtosAutoComplete);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<AssignmentDto> get(@PathVariable Long id){
        Assignment assignment = assignmentRepository.findByIdAndTeacherId(id, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Assignment not found"));

        ApiMessageDto<AssignmentDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(assignmentMapper.fromEntityToDto(assignment));
        apiMessageDto.setMessage("Get assignment successfully");

        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateAssignmentForm form, BindingResult result){
        lessonRepository.findByIdAndTeacherId(form.getLessonId(), getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("Lesson not found"));

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Assignment assignment = assignmentMapper.fromCreateFormToEntity(form);
        assignmentRepository.save(assignment);

        apiMessageDto.setMessage("Create assignment successfully");

        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateAssignmentForm form, BindingResult result){
        Assignment assignment = assignmentRepository.findByIdAndTeacherId(form.getId(), getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Assignment not found"));

        assignmentMapper.fromUpdateFormToEntity(form, assignment);
        assignmentRepository.save(assignment);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update assignment successfully");

        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable Long id){
        Assignment assignment = assignmentRepository.findByIdAndTeacherId(id, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Assignment not found"));

        assignmentRepository.deleteById(assignment.getId());

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete assignment successfully");

        return apiMessageDto;
    }


    private ApiMessageDto<ResponseListObj<AssignmentDto>> getAssignmentsMessageDto(
            AssignmentCriteria criteria, Pageable pageable, Function<List<Assignment>, List<AssignmentDto>> mapper) {
        Page<Assignment> syllabusPage = assignmentRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListObj<AssignmentDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(mapper.apply(syllabusPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(syllabusPage.getTotalPages());
        responseListObj.setTotalElements(syllabusPage.getTotalElements());

        ApiMessageDto<ResponseListObj<AssignmentDto>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("Get list assignment successfully");

        return apiMessageDto;
    }
}
