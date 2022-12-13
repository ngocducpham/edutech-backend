package com.edutech.api.controller;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.assignmentclass.AssignmentClassDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.form.assignmentclass.CreateAssignmentClassForm;
import com.edutech.api.form.assignmentclass.UpdateAssignmentClassForm;
import com.edutech.api.mapper.AssignmentClassMapper;
import com.edutech.api.storage.criteria.AssignmentClassCriteria;
import com.edutech.api.storage.model.AssignmentClass;
import com.edutech.api.storage.model.Class;
import com.edutech.api.storage.model.Lesson;
import com.edutech.api.storage.repository.AssignmentClassRepository;
import com.edutech.api.storage.repository.AssignmentRepository;
import com.edutech.api.storage.repository.ClassRepository;
import com.edutech.api.storage.repository.LessonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/class-assignment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AssignmentClassController extends ABasicController {
    @Autowired
    private AssignmentClassRepository repository;
    @Autowired
    private AssignmentClassMapper mapper;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<AssignmentClassDto>> getList(AssignmentClassCriteria criteria, Pageable pageable){

        Page<AssignmentClass> listAssignment = repository.findAll(criteria.getSpecification(), pageable);

        ResponseListObj<AssignmentClassDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(mapper.fromEntitiesToDtos(listAssignment.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listAssignment.getTotalPages());
        responseListObj.setTotalElements(listAssignment.getTotalElements());

        ApiMessageDto<ResponseListObj<AssignmentClassDto>> response = new ApiMessageDto<>();
        response.setData(responseListObj);
        response.setMessage("Get class assignment success");

        return response;
    }

    @GetMapping(value = "client/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<AssignmentClassDto> clientGet(@PathVariable Long id){
        AssignmentClass assignment = null;
        if(isStudent()) {
            assignment = repository.findByIdAndStatus(id, EduTechConstant.ASSIGNMENT_CLASS_SHOW);
            if(assignment == null) {
                throw new NotFoundException("Class assignment not found");
            }
        }
        if(isTeacher()){
            assignment = repository.findById(id).orElseThrow(() -> new NotFoundException("Class assignment not found"));
        }

        ApiMessageDto<AssignmentClassDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(mapper.fromEntityToDto(assignment));
        apiMessageDto.setMessage("Get class assignment successfully");

        return apiMessageDto;
    }

    @GetMapping(value = "client/class/{classId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<AssignmentClassDto>> clientGetByClass(@PathVariable Long classId, Pageable pageable){

        ApiMessageDto<ResponseListObj<AssignmentClassDto>> response = new ApiMessageDto<>();
        ResponseListObj<AssignmentClassDto> responseListObj = new ResponseListObj<>();

        if(isStudent()) {
            Class aClass = classRepository.findByIdAndStudentIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Class not found"));

            Page<AssignmentClass> listAssignment = repository.findAllByStudentIdAAndClassIdAndStatus(getCurrentUserId(), classId, pageable);

            responseListObj.setData(mapper.fromEntitiesToDtos(listAssignment.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(listAssignment.getTotalPages());
            responseListObj.setTotalElements(listAssignment.getTotalElements());

            response.setData(responseListObj);
            response.setMessage("Student get class assignments success");
        } else if(isTeacher()) {
            Class aClass = classRepository.findByIdAndTeacherIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Class not found"));

            Page<AssignmentClass> listAssignment = repository.findAllByClassId(classId, pageable);

            responseListObj.setData(mapper.fromEntitiesToDtos(listAssignment.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(listAssignment.getTotalPages());
            responseListObj.setTotalElements(listAssignment.getTotalElements());

            response.setData(responseListObj);
            response.setMessage("Teacher get class assignments success");
        }

        return response;
    }

    @GetMapping(value = "client/lesson/{classId}/{lessonId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<AssignmentClassDto>> clientGetByLesson(@PathVariable Long classId, @PathVariable Long lessonId, Pageable pageable){

        ApiMessageDto<ResponseListObj<AssignmentClassDto>> response = new ApiMessageDto<>();
        ResponseListObj<AssignmentClassDto> responseListObj = new ResponseListObj<>();
        Class cls = classRepository.findById(classId)
                .orElseThrow(()-> new NotFoundException("Class not found"));

        if(isStudent()) {
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new NotFoundException("Lesson not found"));

            Page<AssignmentClass> listAssignment = repository.findAllByLessonId(lessonId, classId, pageable);

            responseListObj.setData(mapper.fromEntitiesToDtos(listAssignment.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(listAssignment.getTotalPages());
            responseListObj.setTotalElements(listAssignment.getTotalElements());

            response.setData(responseListObj);
            response.setMessage("Student get lesson assignments success");
        } else if(isTeacher()) {
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new NotFoundException("Lesson not found"));

            Page<AssignmentClass> listAssignment = repository.findAllByLessonId(lessonId, classId, pageable);

            responseListObj.setData(mapper.fromEntitiesToDtos(listAssignment.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(listAssignment.getTotalPages());
            responseListObj.setTotalElements(listAssignment.getTotalElements());

            response.setData(responseListObj);
            response.setMessage("Teacher get lesson assignments success");
        }

        return response;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateAssignmentClassForm form, BindingResult result){

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        AssignmentClass assignment = mapper.fromCreateFormToEntity(form);
        repository.save(assignment);

        apiMessageDto.setMessage("Create class assignment successfully");

        return apiMessageDto;
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> clientUpdate(@Valid @RequestBody UpdateAssignmentClassForm form, BindingResult result){

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        if(isTeacher()) {
            AssignmentClass assignment = repository.findById(form.getId()).orElseThrow(() -> new NotFoundException("Class assignment not found"));
            mapper.fromUpdateFormToEntity(form, assignment);
            repository.save(assignment);
            apiMessageDto.setMessage("Update class assignment successfully");
        } else {
            apiMessageDto.setMessage("Update class assignment failed");
        }

        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable Long id){

        AssignmentClass assignment = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Class assignment not found"));
        repository.deleteById(assignment.getId());

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete class assignment successfully");

        return apiMessageDto;
    }
}
