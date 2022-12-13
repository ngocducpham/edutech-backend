package com.edutech.api.controller;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.classnews.ClassNewsDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.form.classform.CreateClassForm;
import com.edutech.api.form.classnews.CreateClassNewsForm;
import com.edutech.api.form.classnews.UpdateClassNewsForm;
import com.edutech.api.mapper.ClassNewsMapper;
import com.edutech.api.storage.criteria.ClassNewsCriteria;
import com.edutech.api.storage.model.Class;
import com.edutech.api.storage.model.ClassNews;
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
import java.util.Date;

@RestController
@RequestMapping("/v1/classnews")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ClassNewsController extends ABasicController{
    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassNewsMapper newsMapper;

    @Autowired
    private ClassNewsRepository newsRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateClassNewsForm form, BindingResult result) {
        classRepository.findByIdAndTeacherIdAndStatus(form.getClassId(), getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                .orElseThrow(() -> new NotFoundException("Class not found"));

        ClassNews news = newsMapper.fromCreateFormToEntity(form);
        news.setCreated(new Date());
        news.setStatus(EduTechConstant.STATUS_ACTIVE);
        newsRepository.save(news);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Create class news success");

        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ClassNewsDto>> getList(ClassNewsCriteria criteria, Pageable pageable) {

        ApiMessageDto<ResponseListObj<ClassNewsDto>> apiMessageDto = new ApiMessageDto<>();

        Page<ClassNews> newsPage = newsRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListObj<ClassNewsDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(newsMapper.fromEntitiesToDtos(newsPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(newsPage.getTotalPages());
        responseListObj.setTotalElements(newsPage.getTotalElements());

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("Get list success");

        return apiMessageDto;
    }

    @GetMapping(value = "/client/news-list/{classId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ClassNewsDto>> clientGetClassNewsList(@PathVariable Long classId, ClassNewsCriteria criteria, Pageable pageable) {

        ApiMessageDto<ResponseListObj<ClassNewsDto>> apiMessageDto = new ApiMessageDto<>();

        criteria.setClassId(classId);
        if(isTeacher()){
            Class aClass = classRepository.findByIdAndTeacherIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Teacher not found"));
        }
        else if (isStudent()){
            Class aClass = classRepository.findByIdAndStudentIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Student not found"));
        }
        Page<ClassNews> newsPage = newsRepository.findAllByClassId(classId, pageable);

        ResponseListObj<ClassNewsDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(newsMapper.fromEntitiesToDtos(newsPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(newsPage.getTotalPages());
        responseListObj.setTotalElements(newsPage.getTotalElements());

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("Get class news list success");

        return apiMessageDto;
    }

    @GetMapping(value = "/client/get/{classId}/{classnewsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ClassNewsDto> clientGetClassNews(@PathVariable Long classId, @PathVariable Long classnewsId, ClassNewsCriteria criteria, Pageable pageable) {

        ApiMessageDto<ClassNewsDto> apiMessageDto = new ApiMessageDto<>();

        ClassNews news = newsRepository
                .findByIdAndClassIdAndStatus(classnewsId, classId, EduTechConstant.STATUS_ACTIVE)
                .orElseThrow(()-> new NotFoundException("News not found"));

        apiMessageDto.setData(newsMapper.fromEntityToDto(news));
        apiMessageDto.setMessage("Get class news success");

        return apiMessageDto;
    }

    @GetMapping(value = "/client/news-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ClassNewsDto>> clientGetNews(ClassNewsCriteria criteria, Pageable pageable) {

        ApiMessageDto<ResponseListObj<ClassNewsDto>> apiMessageDto = new ApiMessageDto<>();
        Page<ClassNews> newsPage = null;
        if(isTeacher()){
            newsPage = newsRepository.findAllByTeacherId(getCurrentUserId(), pageable);
            apiMessageDto.setMessage("Teacher Get class news list success");
        }
        else if (isStudent()){
            newsPage = newsRepository.findAllByStudentId(getCurrentUserId(), pageable);
            apiMessageDto.setMessage("Student Get class news list success");
        }

        ResponseListObj<ClassNewsDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(newsMapper.fromEntitiesToDtos(newsPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(newsPage.getTotalPages());
        responseListObj.setTotalElements(newsPage.getTotalElements());

        apiMessageDto.setData(responseListObj);

        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ClassNewsDto> get(@PathVariable Long id) {
        ClassNews news;
        news = newsRepository.findById(id).orElse(null);

        if(isTeacher()){
            Class aClass = classRepository.findByIdAndTeacherIdAndStatus(news.getAClass().getId(), getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Teacher not found"));
        }
        else if (isStudent()){
            Class aClass = classRepository.findByIdAndStudentIdAndStatus(news.getAClass().getId(), getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Student not found"));
        }

        if(news == null){
            throw new NotFoundException("Class not found");
        }

        ApiMessageDto<ClassNewsDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(newsMapper.fromEntityToDto(news));
        apiMessageDto.setMessage("Get news success");

        return apiMessageDto;
    }

    @PutMapping(value = "/client/update/{classnewsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> clientUpdate(@PathVariable Long classnewsId, @Valid @RequestBody UpdateClassNewsForm form, BindingResult result) {
        ClassNews news = newsRepository.findById(classnewsId)
                .orElseThrow(() -> new NotFoundException("News not found"));

        newsMapper.fromUpdateFormToEntity(form, news);
        newsRepository.save(news);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update news success");

        return apiMessageDto;
    }

    // teacher, subject co ton tai
    @DeleteMapping(value = "/client/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable Long id) {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        ClassNews news = newsRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("News not found"));

        if(isTeacher()){
            Class aclass = classRepository.findByIdAndTeacherIdAndStatus(news.getAClass().getId(), getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));
            newsRepository.deleteById(news.getId());
            apiMessageDto.setMessage("Delete news success");
        }

        return apiMessageDto;
    }
}
