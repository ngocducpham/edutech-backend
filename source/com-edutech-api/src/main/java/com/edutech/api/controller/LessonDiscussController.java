package com.edutech.api.controller;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.lessondiscuss.LessonDiscussDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.form.lessondiscuss.CreateLessonDiscussForm;
import com.edutech.api.form.lessondiscuss.UpdateLessonDiscussForm;
import com.edutech.api.mapper.LessonDiscussMapper;
import com.edutech.api.storage.criteria.LessonDiscussCriteria;
import com.edutech.api.storage.model.Account;
import com.edutech.api.storage.model.Class;
import com.edutech.api.storage.model.Lesson;
import com.edutech.api.storage.model.LessonDiscuss;
import com.edutech.api.storage.repository.AccountRepository;
import com.edutech.api.storage.repository.ClassRepository;
import com.edutech.api.storage.repository.LessonDiscussRepository;
import com.edutech.api.storage.repository.LessonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/v1/discuss")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class LessonDiscussController extends ABasicController {
    @Autowired
    private LessonDiscussRepository repository;
    @Autowired
    private LessonDiscussMapper discussmapper;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<LessonDiscussDto>> getList (@Valid LessonDiscussCriteria criteria, Pageable pageable, BindingResult result) {
        ApiMessageDto<ResponseListObj<LessonDiscussDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<LessonDiscuss> discussList = repository.findAll(criteria.getSpecification(), pageable);
        ResponseListObj<LessonDiscussDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(discussmapper.fromEntitiesToDtos(discussList.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(discussList.getTotalPages());
        responseListObj.setTotalElements(discussList.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{discussId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<LessonDiscussDto> get(@PathVariable Long discussId) {
        LessonDiscuss discuss = repository.findById(discussId)
                .orElseThrow(()-> new NotFoundException("Discuss not found"));
        ApiMessageDto<LessonDiscussDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(discussmapper.fromEntityToDto(discuss));
        apiMessageDto.setMessage("Get discuss success");

        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateLessonDiscussForm form, BindingResult result) {
        lessonRepository.findById(form.getLessonId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        classRepository.findById(form.getClassId())
                .orElseThrow(()-> new NotFoundException("Class not found"));
        accountRepository.findById(form.getUserId())
                .orElseThrow(()-> new NotFoundException("User not found"));
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        form.setCreated_date(new Date());
        LessonDiscuss discuss = discussmapper.fromCreateFormToEntity(form);
        discuss.setStatus(EduTechConstant.STATUS_ACTIVE);

        repository.save(discuss);

        apiMessageDto.setMessage("Create discuss successfully");

        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateLessonDiscussForm lessondiscussForm, BindingResult result){
        LessonDiscuss discuss = repository.findById(lessondiscussForm.getId())
                .orElseThrow(()-> new NotFoundException("Discuss not found"));
        lessonRepository.findById(lessondiscussForm.getLessonId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        classRepository.findById(lessondiscussForm.getClassId())
                .orElseThrow(()-> new NotFoundException("Class not found"));
        accountRepository.findById(lessondiscussForm.getUserId())
                .orElseThrow(()-> new NotFoundException("User not found"));
        discussmapper.fromUpdateFormToEntity(lessondiscussForm, discuss);

        discuss.setCreated(new Date());
        discuss = repository.save(discuss);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update lesson success");

        return apiMessageDto;
    }
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        LessonDiscuss discuss = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Lesson not found"));

        repository.delete(discuss);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete discuss success");

        return apiMessageDto;
    }
    @GetMapping(value = "/client/get/{discussId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<LessonDiscussDto> clientGet(@PathVariable Long discussId) {
        ApiMessageDto<LessonDiscussDto> apiMessageDto = new ApiMessageDto<>();
        LessonDiscuss discuss = repository.findById(discussId)
                .orElseThrow(()-> new NotFoundException("Discuss not found"));
        Lesson lesson = lessonRepository.findById(discuss.getLesson().getId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        if(isTeacher()){
            classRepository.findByIdAndTeacherIdAndStatus(discuss.getAclass().getId(), getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));

            apiMessageDto.setData(discussmapper.fromEntityToDto(discuss));
            apiMessageDto.setMessage("Teacher get discuss success");
        } else if(isStudent()){
            classRepository.findByIdAndStudentIdAndStatus(discuss.getAclass().getId(), getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));

            apiMessageDto.setData(discussmapper.fromEntityToDto(discuss));
            apiMessageDto.setMessage("Student get discuss success");
        } else {
            apiMessageDto.setMessage("Client get discuss failed");
        }
        return apiMessageDto;
    }

    @GetMapping(value = "/client/get/class-discuss-list/{classId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<LessonDiscussDto>> clientGetClassList (@Valid LessonDiscussCriteria criteria, @PathVariable Long classId, Pageable pageable, BindingResult result) {

        ApiMessageDto<ResponseListObj<LessonDiscussDto>> apiMessageDto = new ApiMessageDto<>();

        if(isStudent()){
            Class aclass = classRepository.findByIdAndStudentIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("You are not in this class"));
            criteria.setClassId(classId);

            Page<LessonDiscuss> page = repository.findAllByAclassId(classId, pageable);

            ResponseListObj<LessonDiscussDto> responseListObj = new ResponseListObj<>();
            responseListObj.setData(discussmapper.fromEntitiesToDtos(page.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(page.getTotalPages());
            responseListObj.setTotalElements(page.getTotalElements());

            apiMessageDto.setData(responseListObj);
            apiMessageDto.setMessage("Student get class discuss list success");
        } else if(isTeacher()){
            classRepository.findByIdAndTeacherIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("You are not in this class"));
            criteria.setUserId(getCurrentUserId());
            criteria.setClassId(classId);
            criteria.setStatus(EduTechConstant.STATUS_ACTIVE);

            Page<LessonDiscuss> page = repository.findAllByAclassId(classId, pageable);

            ResponseListObj<LessonDiscussDto> responseListObj = new ResponseListObj<>();
            responseListObj.setData(discussmapper.fromEntitiesToDtos(page.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(page.getTotalPages());
            responseListObj.setTotalElements(page.getTotalElements());

            apiMessageDto.setData(responseListObj);
            apiMessageDto.setMessage("Teacher get class discuss list success");
        } else {
            apiMessageDto.setMessage("Get class list failed");
        }
        return apiMessageDto;
    }

    @GetMapping(value = "/client/get/lesson-discuss-list/{classId}/{lessonId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<LessonDiscussDto>> clientGetLessonList (@Valid LessonDiscussCriteria criteria, @PathVariable Long classId, @PathVariable Long lessonId, Pageable pageable, BindingResult result) {

        ApiMessageDto<ResponseListObj<LessonDiscussDto>> apiMessageDto = new ApiMessageDto<>();

        if(isStudent()){
            Class aclass = classRepository.findByIdAndStudentIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("You are not in this class"));
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(()-> new NotFoundException("Lesson not found"));

            Page<LessonDiscuss> page = repository.findAllByLessonId(lessonId, pageable);

            ResponseListObj<LessonDiscussDto> responseListObj = new ResponseListObj<>();
            responseListObj.setData(discussmapper.fromEntitiesToDtos(page.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(page.getTotalPages());
            responseListObj.setTotalElements(page.getTotalElements());

            apiMessageDto.setData(responseListObj);
            apiMessageDto.setMessage("Student get lesson discuss list success");
        } else if(isTeacher()){
            Class aclass = classRepository.findByIdAndTeacherIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("You are not in this class"));
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(()-> new NotFoundException("Lesson not found"));

            Page<LessonDiscuss> page = repository.findAllByLessonId(lessonId, pageable);

            ResponseListObj<LessonDiscussDto> responseListObj = new ResponseListObj<>();
            responseListObj.setData(discussmapper.fromEntitiesToDtos(page.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(page.getTotalPages());
            responseListObj.setTotalElements(page.getTotalElements());

            apiMessageDto.setData(responseListObj);
            apiMessageDto.setMessage("Teacher get lesson discuss list success");
        } else {
            apiMessageDto.setMessage("Get discuss list failed");
        }
        return apiMessageDto;
    }

    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> clientCreate(@Valid @RequestBody CreateLessonDiscussForm lessondiscussForm, BindingResult result) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Lesson lesson = lessonRepository.findById(lessondiscussForm.getLessonId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        Class aclass = classRepository.findById(lessondiscussForm.getClassId())
                .orElseThrow(()-> new NotFoundException("Class not found"));
        Account user = accountRepository.findById(lessondiscussForm.getUserId())
                .orElseThrow(()-> new NotFoundException("User not found"));
        if(isTeacher()){
            classRepository.findByIdAndTeacherIdAndStatus(aclass.getId(), getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));

            LessonDiscuss discuss = discussmapper.fromCreateFormToEntity(lessondiscussForm);
            discuss.setCreated(new Date());
            repository.save(discuss);

            apiMessageDto.setMessage("Teacher get discuss success");
        } else {
            apiMessageDto.setMessage("Client get discuss failed");
        }
        return apiMessageDto;
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> clientUpdate(@Valid @RequestBody UpdateLessonDiscussForm lessondiscussForm, BindingResult result){
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        LessonDiscuss discuss = repository.findById(lessondiscussForm.getId())
                .orElseThrow(()-> new NotFoundException("Discuss not found"));
        if(isTeacher()){
            classRepository.findByIdAndTeacherIdAndStatus(lessondiscussForm.getClassId(), getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));

            discussmapper.fromUpdateFormToEntity(lessondiscussForm, discuss);
            repository.save(discuss);

            apiMessageDto.setMessage("Teacher update discuss success");
        } else {
            apiMessageDto.setMessage("Client update discuss failed");
        }
        return apiMessageDto;
    }
}
