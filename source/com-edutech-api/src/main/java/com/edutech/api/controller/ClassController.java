package com.edutech.api.controller;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.chapter.ChapterDto;
import com.edutech.api.dto.classdto.ClassDto;
import com.edutech.api.dto.student.StudentDto;
import com.edutech.api.dto.syllabus.SyllabusDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.form.assignmentclass.CreateAssignmentClassForm;
import com.edutech.api.form.classform.ClassStudentForm;
import com.edutech.api.form.classform.CreateClassForm;
import com.edutech.api.form.classform.UpdateClassForm;
import com.edutech.api.mapper.AssignmentClassMapper;
import com.edutech.api.mapper.ClassMapper;
import com.edutech.api.mapper.StudentMapper;
import com.edutech.api.mapper.SyllabusMapper;
import com.edutech.api.storage.criteria.ClassCriteria;
import com.edutech.api.storage.criteria.StudentCriteria;
import com.edutech.api.storage.model.*;
import com.edutech.api.storage.model.Class;
import com.edutech.api.storage.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/class")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ClassController extends ABasicController{
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SyllabusRepository syllabusRepository;
    @Autowired
    private SyllabusMapper syllabusMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentClassRepository assignmentClassRepository;
    @Autowired
    private AssignmentClassMapper assignmentClassMapper;
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ClassDto>> getList(ClassCriteria criteria, Pageable pageable) {

        ApiMessageDto<ResponseListObj<ClassDto>> apiMessageDto = new ApiMessageDto<>();

        if(isTeacher() || isStudent()){
            criteria.setTeacherId(getCurrentUserId());
            criteria.setStatus(EduTechConstant.STATUS_ACTIVE);
        }
        Page<Class> classPage = classRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListObj<ClassDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(classMapper.fromEntitiesToDtos(classPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(classPage.getTotalPages());
        responseListObj.setTotalElements(classPage.getTotalElements());

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("Get list success");

        return apiMessageDto;
    }

    @GetMapping(value = "/client/class-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ClassDto>> clientGetClassList(ClassCriteria criteria, Pageable pageable) {

        ApiMessageDto<ResponseListObj<ClassDto>> apiMessageDto = new ApiMessageDto<>();

        if(isStudent()){
            criteria.setStudentId(getCurrentUserId());
            criteria.setStatus(EduTechConstant.STATUS_ACTIVE);

            Page<Class> classPage = classRepository.findAll(criteria.getSpecification(), pageable);

            ResponseListObj<ClassDto> responseListObj = new ResponseListObj<>();
            responseListObj.setData(classMapper.fromEntitiesToDtos(classPage.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(classPage.getTotalPages());
            responseListObj.setTotalElements(classPage.getTotalElements());

            apiMessageDto.setData(responseListObj);
            apiMessageDto.setMessage("Student get class list success");
        } else if(isTeacher()){
            criteria.setTeacherId(getCurrentUserId());
            criteria.setStatus(EduTechConstant.STATUS_ACTIVE);

            Page<Class> classPage = classRepository.findAll(criteria.getSpecification(), pageable);

            ResponseListObj<ClassDto> responseListObj = new ResponseListObj<>();
            responseListObj.setData(classMapper.fromEntitiesToDtos(classPage.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(classPage.getTotalPages());
            responseListObj.setTotalElements(classPage.getTotalElements());

            apiMessageDto.setData(responseListObj);
            apiMessageDto.setMessage("Teacher get class list success");
        } else {
            apiMessageDto.setMessage("Get class list failed");
        }


        return apiMessageDto;
    }

    @GetMapping(value = "/client/get/{classId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ClassDto> clientGetClass(@PathVariable Long classId, ClassCriteria criteria, Pageable pageable) {

        ApiMessageDto<ClassDto> apiMessageDto = new ApiMessageDto<>();
        Class aClass;
        if(isStudent()){
            criteria.setStudentId(getCurrentUserId());
            criteria.setStatus(EduTechConstant.STATUS_ACTIVE);

            aClass = classRepository
                    .findByIdAndStudentIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));

            apiMessageDto.setData(classMapper.fromEntityToDto(aClass));
            apiMessageDto.setMessage("Student get class list success");
        } else if(isTeacher()){
            criteria.setTeacherId(getCurrentUserId());
            criteria.setStatus(EduTechConstant.STATUS_ACTIVE);

            aClass = classRepository
                    .findByIdAndTeacherIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));

            apiMessageDto.setData(classMapper.fromEntityToDto(aClass));
            apiMessageDto.setMessage("Teacher get class success");
        } else {
            apiMessageDto.setMessage("Get class failed");
        }

        return apiMessageDto;
    }

    @GetMapping(value = "/teacher/my-class", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ClassDto>> teacherGetClassList(ClassCriteria criteria, Pageable pageable) {

        ApiMessageDto<ResponseListObj<ClassDto>> apiMessageDto = new ApiMessageDto<>();

        if(isTeacher()){
            criteria.setTeacherId(getCurrentUserId());
            criteria.setStatus(EduTechConstant.STATUS_ACTIVE);

        Page<Class> classPage = classRepository.findAll(criteria.getSpecification(), pageable);

            ResponseListObj<ClassDto> responseListObj = new ResponseListObj<>();
            responseListObj.setData(classMapper.fromEntitiesToDtos(classPage.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(classPage.getTotalPages());
            responseListObj.setTotalElements(classPage.getTotalElements());

            apiMessageDto.setData(responseListObj);
            apiMessageDto.setMessage("Teacher get class list success");
        } else {
            apiMessageDto.setMessage("Get class list failed");
        }


        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ClassDto> get(@PathVariable Long id) {
        Class aClass;
        if (isTeacher()) {
            aClass = classRepository
                    .findByIdAndTeacherIdAndStatus(id, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElse(null);
        } else {
            aClass = classRepository.findById(id).orElse(null);
        }
        if(aClass == null){
            throw new NotFoundException("Class not found");
        }

        ApiMessageDto<ClassDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(classMapper.fromEntityToDto(aClass));
        apiMessageDto.setMessage("Get class success");

        return apiMessageDto;
    }

    // client get syllabus
    @GetMapping(value = "/client/syllabus/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<SyllabusDto> clientGetSyllabus(@PathVariable Long id) {
        Class aClass;
        Syllabus syllabus;
        if (isTeacher()) {
            aClass = classRepository
                    .findByIdAndTeacherIdAndStatus(id, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));
            syllabus = aClass.getSyllabus();
        } else if (isStudent()) {
            aClass = classRepository
                    .findByIdAndStudentIdAndStatus(id, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));
            syllabus = aClass.getSyllabus();
        } else {
            syllabus = null;
        }

        if(syllabus == null){
            throw new NotFoundException("Syllabus not found");
        }

        ApiMessageDto<SyllabusDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(syllabusMapper.fromEntityToDto(syllabus));
        apiMessageDto.setMessage("Get syllabus success");

        return apiMessageDto;
    }

    // teacher, subject co ton tai
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateClassForm form, BindingResult result) {
        teacherRepository.findByIdAndSubjectId(form.getTeacherId(), form.getSubjectId())
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        Class aClass = classMapper.fromCreateFormToEntity(form);
        classRepository.save(aClass);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Create class success");

        return apiMessageDto;
    }

    // teacher, subject co ton tai
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateClassForm form, BindingResult result) {
        Class cls;

        if(isTeacher()){
            form.setTeacherId(null);
            form.setSubjectId(null);
            form.setStatus(null);
            cls = classRepository
                    .findByIdAndTeacherIdAndStatus(form.getId(), getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Class not found"));

            if(form.getSyllabusId() != null){
                syllabusRepository
                        .findByIdAndTeacherIdAndSubjectId(form.getSyllabusId(), getCurrentUserId(),
                                cls.getSubject().getId())
                        .orElseThrow(() -> new NotFoundException("Syllabus not found"));
            }
        } else {
            form.setSyllabusId(null);
            teacherRepository.findByIdAndSubjectId(form.getTeacherId(), form.getSubjectId())
                    .orElseThrow(() -> new NotFoundException("Teacher not found"));

            cls = classRepository.findById(form.getId())
                    .orElseThrow(() -> new NotFoundException("Class not found"));
        }

        classMapper.fromUpdateFormToEntity(form, cls);
        classRepository.save(cls);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update class success");

        return apiMessageDto;
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> clientUpdate(@Valid @RequestBody UpdateClassForm form, BindingResult result) {
        Class cls;

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        if(isTeacher()){
            form.setTeacherId(null);
            form.setSubjectId(null);
            form.setStatus(null);
            cls = classRepository
                    .findByIdAndTeacherIdAndStatus(form.getId(), getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Class not found"));

            if(form.getSyllabusId() != null){
                if(cls.getSyllabus()==null) {
                    Syllabus syllabus = syllabusRepository
                            .findByIdAndTeacherIdAndSubjectId(form.getSyllabusId(), getCurrentUserId(),
                                    cls.getSubject().getId())
                            .orElseThrow(() -> new NotFoundException("Syllabus not found"));
                    for (Chapter c : syllabus.getChapters()) {
                        for (Lesson l : c.getLessons()) {
                            for (Assignment a : l.getAssignments()) {
                                CreateAssignmentClassForm acform = new CreateAssignmentClassForm();
                                acform.setClassId(form.getId());
                                acform.setAssignmentId(a.getId());
                                acform.setType(a.getType());
                                AssignmentClass ac = assignmentClassMapper.fromCreateFormToEntity(acform);
                                assignmentClassRepository.save(ac);
                            }
                        }
                    }

                    classMapper.fromUpdateFormToEntity(form, cls);
                    classRepository.save(cls);
                    apiMessageDto.setMessage("Update class success");
                }
                else {
                    apiMessageDto.setMessage("Update class failed, this class already have syllabus");
                }
            }
        } else {
            apiMessageDto.setMessage("Update class failed, you are not teacher");
        }

        return apiMessageDto;
    }

    // teacher, subject co ton tai
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable Long id) {
        Class aClass = classRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Class not found"));
        classRepository.deleteById(aClass.getId());

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete class success");

        return apiMessageDto;
    }
    @PostMapping(value = "/student/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> addStudent(@Valid @RequestBody ClassStudentForm form){
        Class cls = classRepository.findById(form.getClassId())
                .orElseThrow(()-> new NotFoundException("Class not found"));

        Map<Long, Long> studentIdMap = cls.getStudents().stream()
                .collect(Collectors.toMap(Student::getId, Student::getId));

        List<Student> students = studentRepository.findAllByIdAndStatus(form.getStudentIds().stream()
                .filter(id -> !studentIdMap.containsKey(id))
                .collect(Collectors.toList()), EduTechConstant.STATUS_ACTIVE);

        cls.getStudents().addAll(students);

        classRepository.save(cls);

        ApiMessageDto<String> response = new ApiMessageDto<>();
        response.setData("Add student successfully");

        return response;
    }

    @DeleteMapping(value = "/student/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@Valid @RequestBody ClassStudentForm form){
        Class cls = classRepository.findById(form.getClassId())
                .orElseThrow(()-> new NotFoundException("Class not found"));

        Map<Long, Long> studentIdsMap = form.getStudentIds().stream()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));

        List<Student> filteredStudent = cls.getStudents().stream()
                .filter(s -> !studentIdsMap.containsKey(s.getId()))
                .collect(Collectors.toList());
        cls.setStudents(filteredStudent);

        classRepository.save(cls);

        ApiMessageDto<String> response = new ApiMessageDto<>();
        response.setData("Remove student successfully");

        return response;
    }

    @GetMapping(value = "/client/student-list/{classId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<StudentDto>> getStudentList(@PathVariable Long classId, Pageable pageable){
        StudentCriteria criteria = new StudentCriteria(){{
            setClassId(classId);
        }};
        Page<Student> studentPage = studentRepository.findAll(criteria.getSpecification(), pageable);
        if(isTeacher()){
            criteria.setTeacherId(getCurrentUserId());
            Class aClass = classRepository
                    .findByIdAndTeacherIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));
            List<Student> studentList = aClass.getStudents();
            // Dong sau se tra ve tat ca sinh vien thay vi chi sinh vien cua lop
            // studentPage = studentRepository.findAll(criteria.getSpecification(), pageable);
            studentPage = new PageImpl<>(studentList);
        }

        ResponseListObj<StudentDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(studentMapper.fromEntitiesToDtos(studentPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(studentPage.getTotalPages());
        responseListObj.setTotalElements(studentPage.getTotalElements());

        ApiMessageDto<ResponseListObj<StudentDto>> response = new ApiMessageDto<>();
        response.setData(responseListObj);
        response.setMessage("Get student list successfully");

        return response;
    }

    @GetMapping(value = "/student-list/{classId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<StudentDto>> adminGetStudentList(@PathVariable Long classId, Pageable pageable){
        StudentCriteria criteria = new StudentCriteria(){{
            setClassId(classId);
        }};
        Page<Student> studentPage = studentRepository.findAll(criteria.getSpecification(), pageable);
        if(isAdmin()){
            criteria.setTeacherId(getCurrentUserId());
            Class aClass = classRepository
                    .findById(classId)
                    .orElseThrow(()-> new NotFoundException("Class not found"));
            List<Student> studentList = aClass.getStudents();
            studentPage = new PageImpl<>(studentList);
        }

        ResponseListObj<StudentDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(studentMapper.fromEntitiesToDtos(studentPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(studentPage.getTotalPages());
        responseListObj.setTotalElements(studentPage.getTotalElements());

        ApiMessageDto<ResponseListObj<StudentDto>> response = new ApiMessageDto<>();
        response.setData(responseListObj);
        response.setMessage("Get student list successfully");

        return response;
    }
}
