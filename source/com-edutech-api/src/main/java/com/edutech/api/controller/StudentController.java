package com.edutech.api.controller;


import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.student.StudentDto;
import com.edutech.api.dto.teacher.TeacherDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.exception.RequestException;
import com.edutech.api.form.student.CreateStudentForm;
import com.edutech.api.form.student.UpdateStudentForm;
import com.edutech.api.form.student.UpdateStudentProfileForm;
import com.edutech.api.mapper.AccountMapper;
import com.edutech.api.mapper.StudentMapper;
import com.edutech.api.storage.criteria.StudentCriteria;
import com.edutech.api.storage.criteria.TeacherCriteria;
import com.edutech.api.storage.model.*;
import com.edutech.api.storage.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/student")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class StudentController extends ABasicController{
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    StudentMapper studentMapper;

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MajorRepository majorRepository;
    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<StudentDto>> getList(StudentCriteria criteria, Pageable pageable){
        Page<Student> studentPage = studentRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListObj<StudentDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(studentMapper.fromEntitiesToDtos(studentPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(studentPage.getTotalPages());
        responseListObj.setTotalElements(studentPage.getTotalElements());

        ApiMessageDto<ResponseListObj<StudentDto>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List student success");

        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<StudentDto> get(@PathVariable("id") Long id){
        Student student = studentRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Student not found"));

        ApiMessageDto<StudentDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(studentMapper.fromEntityToDto(student));
        apiMessageDto.setMessage("Get student success");

        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateStudentForm form, BindingResult result){
        Group group = groupRepository.findFirstByKind(EduTechConstant.GROUP_KIND_STUDENT)
                .orElseThrow(()-> new NotFoundException("Group not found"));
        Major major = majorRepository.findById(form.getMajorId())
                .orElseThrow(()-> new NotFoundException("Major not found"));

        Account account = accountRepository.findAccountByUsername(form.getUsername());
        if (account != null) {
            throw new RequestException("Username already exists");
        }

        provinceCheckExist(form.getProvinceId(), form.getDistrictId(), form.getCommuneId());

        Student student = studentMapper.fromCreateStudentFormToEntity(form);
        student.setMajor(major);

        Account studentAccount = student.getAccount();
        studentAccount.setKind(EduTechConstant.USER_KIND_STUDENT);
        studentAccount.setGroup(group);

        studentRepository.saveAndFlush(student);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Create student success");

        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateStudentForm form, BindingResult result){
        Student student = studentRepository.findById(form.getId())
                .orElseThrow(()-> new NotFoundException("Student not found"));
        majorRepository.findById(form.getMajorId())
                .orElseThrow(()-> new NotFoundException("Major not found"));

        provinceCheckExist(form.getProvinceId(), form.getDistrictId(), form.getCommuneId());

        studentMapper.fromUpdateStudentFormToEntity(form, student);
        studentRepository.save(student);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update student success");

        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        Student student = studentRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Student not found"));

        studentRepository.delete(student);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete student success");

        return apiMessageDto;
    }

    @GetMapping(value = "/my-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<StudentDto> getMyProfile(){
        Student student = studentRepository.findById(getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Invalid user"));

        ApiMessageDto<StudentDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(studentMapper.fromEntityToDtoProfile(student));
        apiMessageDto.setMessage("Get student profile successfully");

        return apiMessageDto;
    }

    @PutMapping(value = "/update-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateProfile(@Valid @RequestBody UpdateStudentProfileForm form, BindingResult result){
        Student student = studentRepository.findById(getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Invalid user"));

        if(!passwordEncoder.matches(form.getCurrentPassword(),student.getAccount().getPassword())){
            throw new RequestException("Current password is incorrect");
        }

        studentMapper.fromUpdateStudentProfileFormToEntity(form, student);
        studentRepository.save(student);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update student profile successfully");

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
    private boolean checkMajor(Student student){
        Major major = student.getMajor();
        Optional<Major> m = majorRepository.findById(major.getId());
        if (m!=null) {
            return true;
        }
        else return false;
    }
    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<StudentDto>> autoComplete(StudentCriteria criteria){
        List<Student> studentPage = studentRepository.findAll(criteria.getSpecification());

        ResponseListObj<StudentDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(studentMapper.fromEntitiesToDtos(studentPage));

        ApiMessageDto<ResponseListObj<StudentDto>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List student auto complete success");

        return apiMessageDto;
    }
}
