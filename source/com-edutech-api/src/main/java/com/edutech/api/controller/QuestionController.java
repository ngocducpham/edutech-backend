package com.edutech.api.controller;

import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.question.QuestionDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.form.question.CreateQuestionForm;
import com.edutech.api.form.question.UpdateQuestionForm;
import com.edutech.api.mapper.QuestionMapper;
import com.edutech.api.storage.criteria.QuestionCriteria;
import com.edutech.api.storage.model.AssignmentClass;
import com.edutech.api.storage.model.Question;
import com.edutech.api.storage.repository.AssignmentClassRepository;
import com.edutech.api.storage.repository.AssignmentRepository;
import com.edutech.api.storage.repository.QuestionRepository;
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
@RequestMapping("/v1/question")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class QuestionController extends ABasicController{

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentClassRepository assignmentClassRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<QuestionDto>> getList(QuestionCriteria criteria, Pageable pageable){
        criteria.setTeacherId(getCurrentUserId());

        Page<Question> questionPage = questionRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListObj<QuestionDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(questionMapper.fromEntitiesToDtos(questionPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(questionPage.getTotalPages());
        responseListObj.setTotalElements(questionPage.getTotalElements());

        ApiMessageDto<ResponseListObj<QuestionDto>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("Get list question successfully");

        return apiMessageDto;
    }
    @GetMapping(value = "/client/get/{assignmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<QuestionDto>> getList(@PathVariable Long assignmentId, Pageable pageable){

        ResponseListObj<QuestionDto> responseListObj = new ResponseListObj<>();
        ApiMessageDto<ResponseListObj<QuestionDto>> apiMessageDto = new ApiMessageDto<>();

        if(isStudent() || isTeacher()) {
            AssignmentClass ac = assignmentClassRepository.findById(assignmentId)
                    .orElseThrow(() -> new NotFoundException("Assignment Class not found"));
            Page<Question> questionPage = questionRepository.findAllByAssignmentId(ac.getAssignment().getId(), pageable);

            responseListObj.setData(questionMapper.fromEntitiesToDtos(questionPage.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(questionPage.getTotalPages());
            responseListObj.setTotalElements(questionPage.getTotalElements());

            apiMessageDto.setData(responseListObj);
            apiMessageDto.setMessage("Client get list question successfully");
        } else {
            apiMessageDto.setMessage("Client get list question failed");
        }

        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<QuestionDto> get(@PathVariable Long id){
        Question question = questionRepository.findByIdAndTeacherId(id, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Question not found"));

        ApiMessageDto<QuestionDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(questionMapper.fromEntityToDto(question));
        apiMessageDto.setMessage("Get question successfully");

        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateQuestionForm form, BindingResult result){
        assignmentRepository.findByIdAndTeacherId(form.getAssignmentId(), getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("Question not found"));

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Question assignment = questionMapper.fromCreateFormToEntity(form);
        questionRepository.save(assignment);

        apiMessageDto.setMessage("Create question successfully");

        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateQuestionForm form, BindingResult result){
        if(form.getId() == null){
            throw new NotFoundException("id is required");
        }

        Question question = questionRepository.findByIdAndTeacherId(form.getId(), getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Question not found"));

        questionMapper.fromUpdateFormToEntity(form, question);
        questionRepository.save(question);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update question successfully");

        return apiMessageDto;
    }

    @PutMapping(value = "/update-all/{assignmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@PathVariable(name ="assignmentId") Long  assignmentId,
                                        @Valid @RequestBody List<UpdateQuestionForm> forms, BindingResult result){

        assignmentRepository.findByIdAndTeacherId(assignmentId, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Assignment not found"));

        questionRepository.saveAll(questionMapper.fromUpdateFormsToEntities(forms, assignmentId));

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update question successfully");

        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable Long id){
        Question question = questionRepository.findByIdAndTeacherId(id, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Question not found"));

        questionRepository.deleteById(question.getId());

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete question successfully");

        return apiMessageDto;
    }

}
