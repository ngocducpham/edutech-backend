package com.edutech.api.controller;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ErrorCode;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.exam.ExamDto;
import com.edutech.api.dto.question.QuestionDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.exception.RequestException;
import com.edutech.api.form.exam.CreateExamForm;
import com.edutech.api.form.exam.UpdateExamForm;
import com.edutech.api.mapper.ExamMapper;
import com.edutech.api.mapper.QuestionMapper;
import com.edutech.api.storage.criteria.ExamCriteria;
import com.edutech.api.storage.model.*;
import com.edutech.api.storage.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/exam")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ExamController extends ABasicController{
    @Autowired
    private ExamRepository repository;
    @Autowired
    private ExamMapper mapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AssignmentClassRepository assignmentClassRepository;
    @Autowired
    private ExamAnswerRepository answerRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ExamDto>> list(ExamCriteria criteria, Pageable pageable) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.NEWS_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<ResponseListObj<ExamDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Exam> list = repository.findAll(criteria.getSpecification(), pageable);
        ResponseListObj<ExamDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(mapper.fromEntitiesToDtos(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get exam list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ExamDto> get(@PathVariable("id") Long id){

        Account currentUser = accountRepository.findById(getCurrentUserId()) .orElse(null);
        if(!isAdmin()) {
            throw new RequestException(ErrorCode.PERMISSION_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<ExamDto> response = new ApiMessageDto<>();
        Exam exam = repository.findById(id).orElseThrow(() -> new NotFoundException("Exam not found!"));
        response.setData(mapper.fromEntityToDto(exam));
        response.setMessage("Get exam success");
        return response;
    }

    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateExamForm form, BindingResult result) {
        ApiMessageDto<String> response = new ApiMessageDto<>();
        if(isStudent()){
            Student student = studentRepository.findById(getCurrentUserId()).orElseThrow(() -> new NotFoundException("Student not found"));

            if(StudentNotSubmitExam(student.getId(), form.getAssignmentClassId())){
                response.setMessage("Last exam has not been submitted");
                return response;
            }
            form.setStudentId(getCurrentUserId());
            Exam exam = mapper.fromCreateFormToEntity(form);
            exam.setAttempt_time(new Date());

            repository.save(exam);
            response.setMessage("Create exam success!");
            return response;
        } else {
            response.setMessage("Create exam failed!");
            return response;
        }
    }

    @PostMapping(value = "/client/submit/{examId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> submit(@PathVariable Long examId) {
        ApiMessageDto<String> response = new ApiMessageDto<>();
        if(isStudent()){
            Exam exam = repository.findById(examId).orElseThrow(() -> new NotFoundException("Exam not found"));
            if(exam.getStudent().getId()!=getCurrentUserId()){
                response.setMessage("Student not found");
                return response;
            }
            exam.setSubmit_time(new Date());
            repository.save(exam);
            response.setMessage("Submit exam success!");
            return response;
        } else {
            response.setMessage("submit exam failed!");
            return response;
        }
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateExamForm form, BindingResult result){
        ApiMessageDto<String> response = new ApiMessageDto<>();
        if(isStudent()){
            Exam exam = repository.findById(form.getId())
                    .orElseThrow(() -> new NotFoundException("Exam not found"));
            mapper.fromUpdateFormToEntity(form, exam);
            response.setMessage("Update exam success!");
            return response;
        } else {
            response.setMessage("Update exam failed!");
            return response;
        }
    }

    @GetMapping(value = "/client/get/{examId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ExamDto> clientGet(@PathVariable Long examId){

        ApiMessageDto<ExamDto> response = new ApiMessageDto<>();

        if(isStudent()){
            Exam exam = repository.findById(examId).orElseThrow(() -> new NotFoundException("Exam not found!"));
            if(exam.getStudent().getId()!=getCurrentUserId()){
                response.setMessage("Your are not the student of the exam");
                return response;
            }
            response.setData(mapper.fromEntityToDto(exam));
            response.setMessage("Get exam success");
        }
        return response;
    }

    @GetMapping(value = "/client/get/assignment/{assignmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ExamDto>> clientGetByAssignment(@PathVariable Long assignmentId, Pageable pageable){
        ApiMessageDto<ResponseListObj<ExamDto>> response = new ApiMessageDto<>();
        if(isStudent()) {

            AssignmentClass ac = assignmentClassRepository.findById(assignmentId).orElseThrow(() -> new NotFoundException("Assignment not found!"));
            Page<Exam> exams = repository.findAllByAssignmentClassIdAndStudentId(assignmentId, getCurrentUserId(), pageable);

            ResponseListObj<ExamDto> responseListObj = new ResponseListObj<>();
            responseListObj.setData(mapper.fromEntitiesToDtos(exams.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(exams.getTotalPages());
            responseListObj.setTotalElements(exams.getTotalElements());

            response.setData(responseListObj);
            response.setMessage("Get exam success");
        } else if(isTeacher()) {
            AssignmentClass ac = assignmentClassRepository.findById(assignmentId).orElseThrow(() -> new NotFoundException("Assignment not found!"));
            Page<Exam> exams = repository.findAllByAssignmentClassId(assignmentId, pageable);

            ResponseListObj<ExamDto> responseListObj = new ResponseListObj<>();
            responseListObj.setData(mapper.fromEntitiesToDtos(exams.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(exams.getTotalPages());
            responseListObj.setTotalElements(exams.getTotalElements());

            response.setData(responseListObj);
            response.setMessage("Get exam success");
        } else {
            response.setMessage("Get exam failed");
        }
        return response;
    }

    @GetMapping(value = "/client/get-point/{examId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Double> clientGetPoint(@PathVariable Long examId){
        Account currentUser = accountRepository.findById(getCurrentUserId()) .orElse(null);
        if(!isStudent() && !isTeacher()) {
            throw new RequestException(ErrorCode.PERMISSION_ERROR_UNAUTHORIZED);
        }
        List<ExamAnswer> answers = answerRepository.findAllByExamId(examId);
        Double point = 0.0;
        for(ExamAnswer ea : answers){
            point += ea.getPoint();
        }

        ApiMessageDto<Double> response = new ApiMessageDto<>();
        Exam exam = repository.findById(examId).orElseThrow(() -> new NotFoundException("Exam not found!"));
        response.setData(point);
        response.setMessage("Get point success");
        return response;
    }

    private Boolean StudentNotSubmitExam(Long studentId, Long assignmentId) {
        Student s = studentRepository.findById(studentId)
                .orElseThrow(()-> new NotFoundException("Student not found"));
        AssignmentClass ac = assignmentClassRepository.findById(assignmentId)
                .orElseThrow(()-> new NotFoundException("AssignmentClass not found"));
        List<Exam> studentExamList = repository.findAllByAssignmentClassIdAndStudentIdList(assignmentId, studentId);
        for(int i = studentExamList.size()-1; i > 0; i--) {
            if(studentExamList.get(i).getSubmit_time()==null){
                return true;
            }
        }
        return false;
    }

    @GetMapping(value = "/client/do-exam/{assignmentClassId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ExamDto> studentDoExamGet(@PathVariable Long assignmentClassId){
        AssignmentClass ac = assignmentClassRepository.findById(assignmentClassId)
                .orElseThrow(()-> new NotFoundException("AssignmentClass not found"));

        ApiMessageDto<ExamDto> response = new ApiMessageDto<>();

        if(isStudent() && StudentNotSubmitExam(getCurrentUserId(), assignmentClassId)){

            List<Exam> studentExamList = repository.findAllByAssignmentClassIdAndStudentIdList(assignmentClassId, getCurrentUserId());

            Long lastExamId = studentExamList.get(studentExamList.size()-1).getId();

            Exam exam = repository.findById(lastExamId).orElseThrow(() -> new NotFoundException("Exam not found!"));
            if(exam.getStudent().getId()!=getCurrentUserId()){
                response.setMessage("Your are not the student of the exam");
                return response;
            }

            List<Question> questionList = questionRepository.findAllByAssignmentIdList(ac.getAssignment().getId());
            List<ExamAnswer> answerList = answerRepository.findAllByExamId(lastExamId);

            response.setData(mapper.fromEntityToDto(exam));
            response.setMessage("Get last exam success");
        } else if(isStudent()) {
            Student student = studentRepository.findById(getCurrentUserId()).orElseThrow(() -> new NotFoundException("Student not found"));

            if(StudentNotSubmitExam(student.getId(), assignmentClassId)){
                response.setMessage("Last exam has not been submitted");
                return response;
            }
            CreateExamForm createExamForm = new CreateExamForm();
            createExamForm.setAssignmentClassId(assignmentClassId);
            createExamForm.setStudentId(getCurrentUserId());
            Exam exam = mapper.fromCreateFormToEntity(createExamForm);
            exam.setAttempt_time(new Date());

            List<Question> questionList = questionRepository.findAllByAssignmentIdList(ac.getAssignment().getId());
            List<QuestionDto> questionDtoList = questionMapper.fromEntitiesToDtos(questionList);
            ExamDto examDTO = mapper.fromEntityToDto(exam);
            examDTO.setQuestionList(questionDtoList);
            repository.save(exam);

            response.setData(examDTO);
            response.setMessage("Create exam success!");
            return response;
        }
        else {
            response.setMessage("Nothing are on process");
        }
        return response;
    }

    @PostMapping(value = "/client/do-exam/{assignmentClassId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ExamDto> studentDoExamPost(@PathVariable Long assignmentClassId){
        AssignmentClass ac = assignmentClassRepository.findById(assignmentClassId)
                .orElseThrow(()-> new NotFoundException("AssignmentClass not found"));

        ApiMessageDto<ExamDto> response = new ApiMessageDto<>();

        if(isStudent() && StudentNotSubmitExam(getCurrentUserId(), assignmentClassId)){

            List<Exam> studentExamList = repository.findAllByAssignmentClassIdAndStudentIdList(assignmentClassId, getCurrentUserId());

            Long lastExamId = studentExamList.get(studentExamList.size()-1).getId();

            Exam exam = repository.findById(lastExamId).orElseThrow(() -> new NotFoundException("Exam not found!"));
            if(exam.getStudent().getId()!=getCurrentUserId()){
                response.setMessage("Your are not the student of the exam");
                return response;
            }

            List<Question> questionList = questionRepository.findAllByAssignmentIdList(ac.getAssignment().getId());
            List<ExamAnswer> answerList = answerRepository.findAllByExamId(lastExamId);

            response.setData(mapper.fromEntityToDto(exam));
            response.setMessage("Get last exam success");
        } else if(isStudent()) {
            Student student = studentRepository.findById(getCurrentUserId()).orElseThrow(() -> new NotFoundException("Student not found"));

            if(StudentNotSubmitExam(student.getId(), assignmentClassId)){
                response.setMessage("Last exam has not been submitted");
                return response;
            }
            CreateExamForm createExamForm = new CreateExamForm();
            createExamForm.setAssignmentClassId(assignmentClassId);
            createExamForm.setStudentId(getCurrentUserId());
            Exam exam = mapper.fromCreateFormToEntity(createExamForm);
            exam.setAttempt_time(new Date());

            List<Question> questionList = questionRepository.findAllByAssignmentIdList(ac.getAssignment().getId());
            List<QuestionDto> questionDtoList = questionMapper.fromEntitiesToDtos(questionList);
            ExamDto examDTO = mapper.fromEntityToDto(exam);
            examDTO.setQuestionList(questionDtoList);
            repository.save(exam);

            response.setData(examDTO);
            response.setMessage("Create exam success!");
            return response;
        }
        else {
            response.setMessage("Nothing are on process");
        }
        return response;
    }

    @DeleteMapping(value = "/delete/{examId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete (@PathVariable Long examId){
        Exam exam = repository.findById(examId)
                .orElseThrow(()->new NotFoundException("Exam not found"));
        repository.delete(exam);

        ApiMessageDto<String> response = new ApiMessageDto<>();
        response.setMessage("Delete exam success");
        return response;
    }

}
