package com.edutech.api.controller;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.subject.SubjectDto;
import com.edutech.api.dto.teacher.TeacherDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.exception.RequestException;
import com.edutech.api.form.teacher.CreateTeacherForm;
import com.edutech.api.form.teacher.TeacherSubjectForm;
import com.edutech.api.form.teacher.UpdateTeacherForm;
import com.edutech.api.form.teacher.UpdateTeacherProfileForm;
import com.edutech.api.mapper.AccountMapper;
import com.edutech.api.mapper.SubjectMapper;
import com.edutech.api.mapper.TeacherMapper;
import com.edutech.api.storage.criteria.SubjectCriteria;
import com.edutech.api.storage.criteria.TeacherCriteria;
import com.edutech.api.storage.model.*;
import com.edutech.api.storage.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/teacher")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class TeacherController extends ABasicController {

    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    TeacherMapper teacherMapper;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    SubjectMapper subjectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<TeacherDto>> getList(TeacherCriteria criteria, Pageable pageable){
        Page<Teacher> teacherPage = teacherRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListObj<TeacherDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(teacherMapper.fromEntitiesToDtos(teacherPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(teacherPage.getTotalPages());
        responseListObj.setTotalElements(teacherPage.getTotalElements());

        ApiMessageDto<ResponseListObj<TeacherDto>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List teacher success");

        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<TeacherDto> get(@PathVariable("id") Long id){
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Teacher not found"));

        ApiMessageDto<TeacherDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(teacherMapper.fromEntityToDto(teacher));
        apiMessageDto.setMessage("Get teacher success");

        return apiMessageDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<TeacherDto>> autoComplete(TeacherCriteria criteria){
        List<Teacher> teacherPage = teacherRepository.findAll(criteria.getSpecification());

        ResponseListObj<TeacherDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(teacherMapper.fromEntitiesToDtosAutoComplete(teacherPage));

        ApiMessageDto<ResponseListObj<TeacherDto>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List teacher auto complete success");

        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateTeacherForm form, BindingResult result){
        Group group = groupRepository.findFirstByKind(EduTechConstant.GROUP_KIND_TEACHER)
                .orElseThrow(()-> new NotFoundException("Group not found"));

        Account account = accountRepository.findAccountByUsername(form.getUsername());
        if (account != null) {
            throw new RequestException("Username already exists");
        }

        provinceCheckExist(form.getProvinceId(), form.getDistrictId(), form.getCommuneId());

        Teacher teacher = teacherMapper.fromCreateTeacherFormToEntity(form);
        Account teacherAccount = teacher.getAccount();
        teacherAccount.setGroup(group);
        teacherAccount.setKind(EduTechConstant.USER_KIND_TEACHER);

        teacherRepository.saveAndFlush(teacher);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Create teacher success");

        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateTeacherForm form, BindingResult result){
        Teacher teacher = teacherRepository.findById(form.getId())
                .orElseThrow(()-> new NotFoundException("Teacher not found"));

        provinceCheckExist(form.getProvinceId(), form.getDistrictId(), form.getCommuneId());

        teacherMapper.fromUpdateTeacherFormToEntity(form, teacher);
        teacherRepository.save(teacher);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update teacher success");

        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Teacher not found"));

        teacherRepository.delete(teacher);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete teacher success");

        return apiMessageDto;
    }

    @GetMapping(value = "/subject/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<SubjectDto>> getList(SubjectCriteria criteria, Pageable pageable, BindingResult result){
        if(criteria.getTeacherId() == null){
            throw new RequestException("Teacher id is required");
        }
        criteria.setStatus(EduTechConstant.STATUS_ACTIVE);

        teacherRepository.findById(criteria.getTeacherId())
                .orElseThrow(()-> new NotFoundException("Teacher not found"));

        Page<Subject> subject = subjectRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListObj<SubjectDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(subjectMapper.fromEntitiesToDtos(subject.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(subject.getTotalPages());
        responseListObj.setTotalElements(subject.getTotalElements());

        ApiMessageDto<ResponseListObj<SubjectDto>> response = new ApiMessageDto<>();
        response.setData(responseListObj);
        response.setMessage("Get subjects successfully");

        return response;
    }

    @PostMapping(value = "/subject/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> add(@Valid @RequestBody TeacherSubjectForm form){
        Teacher teacher = teacherRepository.findById(form.getTeacherId())
                .orElseThrow(()-> new NotFoundException("Teacher not found"));

        Map<Long, Subject> teacherSubjectMap = teacher.getSubjects().stream()
                .collect(Collectors.toMap(Subject::getId, Function.identity()));

        List<Subject> subjects = subjectRepository.findAllByIdAndStatus(form.getSubjectIds().stream()
                .filter(subjectId -> !teacherSubjectMap.containsKey(subjectId))
                .collect(Collectors.toList()), EduTechConstant.STATUS_ACTIVE);

        teacher.getSubjects().addAll(subjects);

        teacherRepository.save(teacher);

        ApiMessageDto<String> response = new ApiMessageDto<>();
        response.setData("Add subject successfully");

        return response;
    }

    @DeleteMapping(value = "/subject/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@Valid @RequestBody TeacherSubjectForm form){
        Teacher teacher = teacherRepository.findById(form.getTeacherId())
                .orElseThrow(()-> new NotFoundException("Teacher not found"));

        Map<Long, Long> subjectIdsMap = form.getSubjectIds().stream()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));

        List<Subject> filteredSubject = teacher.getSubjects().stream()
                .filter(s -> !subjectIdsMap.containsKey(s.getId()))
                .collect(Collectors.toList());
        teacher.setSubjects(filteredSubject);

        teacherRepository.save(teacher);

        ApiMessageDto<String> response = new ApiMessageDto<>();
        response.setData("Remove subject successfully");

        return response;
    }

    @GetMapping(value = "/my-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<TeacherDto> getMyProfile(){
        Teacher teacher = teacherRepository.findById(getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Invalid user"));

        ApiMessageDto<TeacherDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(teacherMapper.fromEntityToDtoProfile(teacher));
        apiMessageDto.setMessage("Get teacher profile successfully");

        return apiMessageDto;
    }

    @GetMapping(value = "/my-subject", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<SubjectDto>> getMySubject(SubjectCriteria criteria, Pageable pageable, BindingResult result){
        if(!isTeacher()){
            throw new RequestException("Invalid user");
        }
        criteria.setTeacherId(getCurrentUserId());

        Page<Subject> subject = subjectRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListObj<SubjectDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(subjectMapper.fromEntitiesToDtos(subject.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(subject.getTotalPages());
        responseListObj.setTotalElements(subject.getTotalElements());

        ApiMessageDto<ResponseListObj<SubjectDto>> response = new ApiMessageDto<>();
        response.setData(responseListObj);
        response.setMessage("Get subjects successfully");

        return response;
    }

    @PutMapping(value = "/update-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateProfile(@Valid @RequestBody UpdateTeacherProfileForm form){
        Teacher teacher = teacherRepository.findById(getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Invalid user"));

        if(!passwordEncoder.matches(form.getCurrentPassword(),teacher.getAccount().getPassword())){
            throw new RequestException("Current password is incorrect");
        }

        teacherMapper.fromUpdateTeacherProfileFormToEntity(form, teacher);
        teacherRepository.save(teacher);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update teacher profile success");

        return apiMessageDto;
    }

    private Province[] provinceCheckExist(Long provinceId, Long districtId, Long communeId){
        Province province, district = null, commune = null;

        province = provinceRepository.findById(provinceId)
                .orElseThrow(()-> new NotFoundException("Province not found"));
        if(districtId != null){
            district = provinceRepository.findById(districtId)
                    .orElseThrow(()-> new NotFoundException("District not found"));
            if (!district.getParentProvince().getProvinceId().equals(provinceId)){
                throw new NotFoundException("District not found");
            }
        }
        if (communeId != null){
            commune = provinceRepository.findById(communeId)
                    .orElseThrow(()-> new NotFoundException("Commune not found"));
            if (!commune.getParentProvince().getProvinceId().equals(districtId)){
                throw new NotFoundException("Commune not found");
            }
        }

        return new Province[]{province, district, commune};
    }
}
