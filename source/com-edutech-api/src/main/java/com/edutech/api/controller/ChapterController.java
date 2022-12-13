package com.edutech.api.controller;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.chapter.ChapterDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.form.chapter.CreateChapterForm;
import com.edutech.api.form.chapter.UpdateChapterForm;
import com.edutech.api.mapper.ChapterMapper;
import com.edutech.api.storage.criteria.ChapterCriteria;
import com.edutech.api.storage.model.Chapter;
import com.edutech.api.storage.model.Class;
import com.edutech.api.storage.model.Syllabus;
import com.edutech.api.storage.repository.ChapterRepository;
import com.edutech.api.storage.repository.ClassRepository;
import com.edutech.api.storage.repository.SyllabusRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/chapter")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ChapterController extends ABasicController{
    @Autowired
    ChapterRepository chapterRepository;
    @Autowired
    SyllabusRepository syllabusRepository;
    @Autowired
    ChapterMapper chapterMapper;
    @Autowired
    ClassRepository classRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ChapterDto>> getList(@Valid ChapterCriteria criteria, Pageable pageable, BindingResult result) {
        ApiMessageDto<ResponseListObj<ChapterDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        criteria.setTeacherId(getCurrentUserId());
        Page<Chapter> listChapter = chapterRepository.findAll(criteria.getSpecification(), pageable);
        ResponseListObj<ChapterDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(chapterMapper.fromEntitiesToDtos(listChapter.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listChapter.getTotalPages());
        responseListObj.setTotalElements(listChapter.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{chapterId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ChapterDto> get(@PathVariable Long chapterId) {
        Chapter chapter = chapterRepository.findByIdAndTeacherId(chapterId, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Chapter not found"));
        ApiMessageDto<ChapterDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(chapterMapper.fromEntityToDtoDetail(chapter));
        apiMessageDto.setMessage("Get chapter success");

        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateChapterForm chapterForm, BindingResult result) {
        syllabusRepository.findByIdAndTeacherId(chapterForm.getSyllabusId(), getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("Syllabus not found"));

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Chapter chapter = chapterMapper.fromCreateFormToEntity(chapterForm);
        chapterRepository.save(chapter);

        apiMessageDto.setMessage("Create chapter successfully");

        return apiMessageDto;
    }
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateChapterForm form, BindingResult result){
        Chapter chapter = chapterRepository.findByIdAndTeacherId(form.getId(), getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Chapter not found"));

        chapterMapper.fromUpdateFormToEntity(form, chapter);
        chapter = chapterRepository.save(chapter);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update chapter success");

        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        Chapter chapter = chapterRepository.findByIdAndTeacherId(id, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Chapter not found"));

        chapterRepository.delete(chapter);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete chapter success");

        return apiMessageDto;
    }
    @GetMapping(value = "/client/chapter/{classId}/{chapterId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ChapterDto> clientGetChapter(@PathVariable Long classId, @PathVariable Long chapterId) {
        Class aClass;
        Syllabus syllabus;
        Chapter chapter;
        if (isTeacher()) {
            aClass = classRepository
                    .findByIdAndTeacherIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));
            syllabus = aClass.getSyllabus();
            //chapter = chapterRepository.findByIdAndSyllabusId(chapterId, syllabus.getId()).get();
            chapter = chapterRepository.findById(chapterId).orElseThrow(()-> new NotFoundException("Chapter not found"));
        } else if (isStudent()) {
            aClass = classRepository
                    .findByIdAndStudentIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));
            syllabus = aClass.getSyllabus();
            //chapter = chapterRepository.findByIdAndSyllabusId(chapterId, syllabus.getId()).get();
            chapter = chapterRepository.findById(chapterId).orElseThrow(()-> new NotFoundException("Chapter not found"));
        } else {
            chapter = null;
        }

        if(chapter == null){
            throw new NotFoundException("Chapter not found");
        }

        ApiMessageDto<ChapterDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(chapterMapper.fromEntityToDtoDetail(chapter));
        apiMessageDto.setMessage("Get chapter success");

        return apiMessageDto;
    }
    @GetMapping(value = "/client/class/{classId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ChapterDto>> clientGetListByClass(@PathVariable Long classId, Pageable pageable) {
        ApiMessageDto<ResponseListObj<ChapterDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        ResponseListObj<ChapterDto> responseListObj = new ResponseListObj<>();

        if(isStudent() || isTeacher()) {
            Class c = classRepository.findById(classId)
                    .orElseThrow(() -> new NotFoundException("Class not found"));

            Page<Chapter> listChapter = chapterRepository.findAllBySyllabusId(c.getSyllabus().getId(), pageable);

            responseListObj.setData(chapterMapper.fromEntitiesToDtosDetail(listChapter.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(listChapter.getTotalPages());
            responseListObj.setTotalElements(listChapter.getTotalElements());

            responseListObjApiMessageDto.setData(responseListObj);
            responseListObjApiMessageDto.setMessage("Get list success");
        } else {
            responseListObjApiMessageDto.setMessage("Get list failed");
        }
        return responseListObjApiMessageDto;
    }
}
