package com.edutech.api.controller;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ErrorCode;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.examanswer.ExamAnswerDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.exception.RequestException;
import com.edutech.api.form.exam.UpdateExamForm;
import com.edutech.api.form.examanswer.CreateExamAnswerForm;
import com.edutech.api.form.examanswer.UpdateExamAnswerForm;
import com.edutech.api.mapper.ExamAnswerMapper;
import com.edutech.api.storage.criteria.ExamAnswerCriteria;
import com.edutech.api.storage.model.*;
import com.edutech.api.storage.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/answer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ExamAnswerController extends ABasicController {
    @Autowired
    private ExamAnswerRepository repository;
    @Autowired
    private ExamAnswerMapper mapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ExamAnswerDto>> list(ExamAnswerCriteria criteria, Pageable pageable) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.NEWS_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<ResponseListObj<ExamAnswerDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<ExamAnswer> list = repository.findAll(criteria.getSpecification(), pageable);
        ResponseListObj<ExamAnswerDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(mapper.fromEntitiesToDtos(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get exam answer list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/answer-list/{examId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ExamAnswerDto>> getExamAnswerList(@PathVariable Long examId, Pageable pageable) {
        ApiMessageDto<ResponseListObj<ExamAnswerDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        examRepository.findById(examId)
                .orElseThrow(() -> new NotFoundException("Exam not found!"));
        Page<ExamAnswer> list = repository.findAllByExamId(examId, pageable);

        ResponseListObj<ExamAnswerDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(mapper.fromEntitiesToDtos(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get exam's answer list success!");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ExamAnswerDto> get(@PathVariable("id") Long id) {

        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if (currentUser == null
                || !currentUser.getKind().equals(EduTechConstant.USER_KIND_ADMIN)
                && !currentUser.getKind().equals(EduTechConstant.USER_KIND_EMPLOYEE)
                && !currentUser.getKind().equals(EduTechConstant.USER_KIND_COLLABORATOR)) {
            throw new RequestException(ErrorCode.PERMISSION_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<ExamAnswerDto> response = new ApiMessageDto<>();
        ExamAnswer exam = repository.findById(id).orElseThrow(() -> new NotFoundException("Answer not found!"));
        response.setData(mapper.fromEntityToDto(exam));
        response.setMessage("Get answer success");
        return response;
    }


    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody UpdateExamAnswerForm form, BindingResult result) {
        ApiMessageDto<String> response = new ApiMessageDto<>();
        if (isStudent()) {
            ExamAnswer ea = repository.findByExamIdAndQuestionId(form.getExam_id(), form.getQuestion_id());
            if(ea==null) {
                //create
                form.setAnswer_date(new Date());
                CreateExamAnswerForm createfrm = new CreateExamAnswerForm();
                createfrm.setExam_id(form.getExam_id());
                createfrm.setAnswer(form.getAnswer());
                createfrm.setAnswer_date(new Date());
                createfrm.setQuestion_id(form.getQuestion_id());
                ExamAnswer answer = mapper.fromCreateFormToEntity(createfrm);
                repository.save(answer);
                response.setMessage("Create answer success!");
            } else {
                //update
                mapper.fromUpdateFormToEntity(form, ea);
                repository.save(ea);
                response.setMessage("Update answer success!");
            }
            return response;
        } else {
            response.setMessage("Create/update answer failed!");
            return response;
        }
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateExamAnswerForm form, BindingResult result) {
        ApiMessageDto<String> response = new ApiMessageDto<>();
        if (isStudent()) {
            ExamAnswer exam = repository.findById(form.getId())
                    .orElseThrow(() -> new NotFoundException("Answer not found"));
            mapper.fromUpdateFormToEntity(form, exam);
            repository.save(exam);
            response.setMessage("Update answer success!");
            return response;
        } else {
            response.setMessage("Update answer failed!");
            return response;
        }
    }

    @GetMapping(value = "/client/answer-list/{examId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ExamAnswerDto>> clientGetExamAnswerList(@PathVariable Long examId, Pageable pageable) {

        ApiMessageDto<ResponseListObj<ExamAnswerDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        ResponseListObj<ExamAnswerDto> responseListObj = new ResponseListObj<>();

        if (isStudent()) {
            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> new NotFoundException("Exam not found!"));
            Page<ExamAnswer> list = repository.findAllByExamId(examId, pageable);
            if(exam.getStudent().getId()!=getCurrentUserId()){
                responseListObjApiMessageDto.setMessage("You are not the student of the exam");
            } else {
                responseListObj.setData(mapper.fromEntitiesToDtos(list.getContent()));
                responseListObj.setPage(pageable.getPageNumber());
                responseListObj.setTotalPage(list.getTotalPages());
                responseListObj.setTotalElements(list.getTotalElements());

                responseListObjApiMessageDto.setData(responseListObj);
                responseListObjApiMessageDto.setMessage("Get exam's answer list success!");
            }
        } else {
            responseListObjApiMessageDto.setMessage("Get exam's answer list failed!");
        }
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/client/point/{examId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Double> clientGetExamPoint(@PathVariable Long examId) throws JsonProcessingException {

        Exam exam = examRepository.findById(examId).orElseThrow(()->new NotFoundException("Exam not found"));
        ApiMessageDto<Double> response = new ApiMessageDto<>();
        List<ExamAnswer> answerList = repository.findAllByExamId(examId);
        Double total_point = 0.0;

        if(isStudent()){
            if(exam.getStudent().getId()!=getCurrentUserId()){
                response.setMessage("You are not the student of the exam");
                return response;
            } else {
                for(ExamAnswer ea : answerList) {
                    Question question = questionRepository.findById(ea.getQuestion().getId())
                            .orElseThrow(() -> new NotFoundException("Question not found!"));

                    Double point = 0.0;

                    String jsonFormattedString = ea.getAnswer().replaceAll("\\\\", "");
                    Answer answer = Answer.parseJson(jsonFormattedString);

                    if(ea.getQuestion().getType()==2){
                        point = getPointForSingleAnswersQuestion(answer, question);
                    } else if (ea.getQuestion().getType()==3){
                        point = getPointForMultipleAnswersQuestion(answer, question);
                    }
                    ea.setPoint(point);
                    total_point+= ea.getPoint();
                }

                response.setData(total_point);
                response.setMessage("Student get exam's point success!");
            }
        } else if (isTeacher()) {
            for(ExamAnswer ea : answerList) {
                Question question = questionRepository.findById(ea.getQuestion().getId())
                        .orElseThrow(() -> new NotFoundException("Question not found!"));

                Double point = 0.0;

                String jsonFormattedString = ea.getAnswer().replaceAll("\\\\", "");
                Answer answer = Answer.parseJson(jsonFormattedString);

                if(ea.getQuestion().getType()==2){
                    point = getPointForSingleAnswersQuestion(answer, question);
                } else if (ea.getQuestion().getType()==3){
                    point = getPointForMultipleAnswersQuestion(answer, question);
                }
                ea.setPoint(point);
                total_point+= ea.getPoint();
            }

            response.setData(total_point);
            response.setMessage("Get exam's point success!");
        }
        return response;
    }
    @GetMapping(value = "/client/answer-point/{examId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Double> clientGetExamAnswerPoint(@PathVariable Long examId, Pageable pageable) throws JsonProcessingException {
        ApiMessageDto<Double> responseListObjApiMessageDto = new ApiMessageDto<>();
        ExamAnswer ea = repository.findById(examId)
                .orElseThrow(() -> new NotFoundException("Answer not found"));

        Question question = questionRepository.findById(ea.getQuestion().getId())
                .orElseThrow(() -> new NotFoundException("Question not found!"));

        // Format lai json trong db de parse vao Answer
        String jsonFormattedString = ea.getAnswer().replaceAll("\\\\", "");
        Answer answer = Answer.parseJson(jsonFormattedString);

        Double point = 0.0;
        if(ea.getQuestion().getType()==2){
            point = getPointForSingleAnswersQuestion(answer, question);
        } else if (ea.getQuestion().getType()==3){
            point = getPointForMultipleAnswersQuestion(answer, question);
        }
        ea.setPoint(point);

        repository.save(ea);

        responseListObjApiMessageDto.setData(point);
        responseListObjApiMessageDto.setMessage("Get exam's answer point success!");
        return responseListObjApiMessageDto;
    }

    @DeleteMapping(value = "/delete/{examanswerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete (@PathVariable Long examanswerId){
        ExamAnswer exam = repository.findById(examanswerId)
                .orElseThrow(()->new NotFoundException("Exam not found"));
        repository.delete(exam);

        ApiMessageDto<String> response = new ApiMessageDto<>();
        response.setMessage("Delete answer success");
        return response;
    }


    public static Double getPointForMultipleAnswersQuestion(Answer answer, Question question) {

        int correct_counts = 0;

        Double point = 0.0;
        Double scale = Math.pow(10, 1);

        try {
            // Format lai json trong db de parse vao Answer
            String jsonFormattedString = question.getAnswer().replaceAll("\\\\", "");
            Answer correct_asnwer = Answer.parseJson(jsonFormattedString);

            // lay so cau dung vai bien question_corrects
            List<String> question_correct_answer = new ArrayList<>();
            for (Answer.Content a : correct_asnwer.getContents()) {
                if (a.getRightAnswer()) {
                    question_correct_answer.add(a.getContent());
                }
            }
            // dem so cau tra loi dung
            for (Answer.Content a : answer.getContents()) {
                if(question_correct_answer.contains(a.getContent())){
                    correct_counts++;
                }
            }

            // tinh diem cho moi y dung
            Double point_per_each = question.getPoint() / question_correct_answer.size();

            // lay so cau sai
            int wrong_choose = answer.getContents().size() - correct_counts;

            // tinh diem tru
            Double minus_point = point_per_each * wrong_choose;
            if (minus_point > question.getPoint()) {
                point = 0.0;
            } else {
                point = question.getPoint() - minus_point;
            }

            return Math.round(point * scale) / scale;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Double getPointForSingleAnswersQuestion(Answer answer, Question question) throws JsonProcessingException {
        String answer_content = "";
        for (Answer.Content a : answer.getContents()) {
                answer_content = a.getContent();
        }
        if(answer.getContents().size()>1){
            throw new NotFoundException("Only 1 answer allowed!");
        }
        String jsonFormattedString = question.getAnswer().replaceAll("\\\\", "");
        Answer correct_asnwer = Answer.parseJson(jsonFormattedString);
        for (Answer.Content a : correct_asnwer.getContents()){
            if(answer_content.equals(a.getContent())){
                if (a.getRightAnswer()){
                    return 1.0;
                }
            }
        }
        return 0.0;
    }
}
