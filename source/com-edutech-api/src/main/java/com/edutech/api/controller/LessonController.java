package com.edutech.api.controller;


import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.chapter.ChapterDto;
import com.edutech.api.dto.lesson.LessonDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.form.lesson.CreateLessonForm;
import com.edutech.api.form.lesson.UpdateLessonForm;
import com.edutech.api.mapper.ChapterMapper;
import com.edutech.api.mapper.LessonMapper;
import com.edutech.api.storage.criteria.LessonCriteria;
import com.edutech.api.storage.model.Chapter;
import com.edutech.api.storage.model.Class;
import com.edutech.api.storage.model.Lesson;
import com.edutech.api.storage.model.Syllabus;
import com.edutech.api.storage.repository.ChapterRepository;
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
import java.util.List;

@RestController
@RequestMapping("/v1/lesson")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class LessonController extends ABasicController{
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    ChapterRepository chapterRepository;
    @Autowired
    LessonMapper lessonMapper;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    ChapterMapper chapterMapper;
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<LessonDto>> getList(@Valid LessonCriteria criteria, Pageable pageable, BindingResult result) {
        ApiMessageDto<ResponseListObj<LessonDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        criteria.setTeacherId(getCurrentUserId());
        Page<Lesson> listLesson = lessonRepository.findAll(criteria.getSpecification(), pageable);
        ResponseListObj<LessonDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(lessonMapper.fromEntitiesToDtos(listLesson.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listLesson.getTotalPages());
        responseListObj.setTotalElements(listLesson.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{lessonId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<LessonDto> get(@PathVariable Long lessonId) {
        Lesson lesson = lessonRepository.findByIdAndTeacherId(lessonId, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        ApiMessageDto<LessonDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(lessonMapper.fromEntityToDto(lesson));
        apiMessageDto.setMessage("Get lesson success");

        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateLessonForm lessonForm, BindingResult result) {
        chapterRepository.findByIdAndTeacherId(lessonForm.getChapterId(), getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Chapter not found"));
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Lesson lesson = lessonMapper.fromCreateFormToEntity(lessonForm);
        lessonRepository.save(lesson);

        apiMessageDto.setMessage("Create lesson successfully");

        return apiMessageDto;
    }
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateLessonForm form, BindingResult result){
        Lesson lesson = lessonRepository.findByIdAndTeacherId(form.getId(), getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        lessonMapper.fromUpdateFormToEntity(form, lesson);
        lesson = lessonRepository.save(lesson);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update lesson success");

        return apiMessageDto;
    }
    @PutMapping(value = "/up/{lessonId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> lessonOrderUp(@PathVariable Long lessonId){
        Lesson lesson = lessonRepository.findByIdAndTeacherId(lessonId, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        List<Lesson> lessonList = lessonRepository.findAllByChapterIdOrderByOrder(lesson.getChapter().getId());

        for (int i = 0; i < lessonList.size(); i++) {
            if (lessonList.get(i).getId().equals(lesson.getId())) {
                if (i > 0) {
                    Long prevOrder = lessonList.get(i - 1).getOrder();
                    lessonList.get(i).setOrder(prevOrder);
                    lessonList.get(i - 1).setOrder(lesson.getOrder());

                    lessonRepository.save(lessonList.get(i));
                    lessonRepository.save(lessonList.get(i - 1));
                }
                break;
            }
        }

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update lesson order success");

        return apiMessageDto;
    }

    @PutMapping(value = "/down/{lessonId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> lessonOrderDown(@PathVariable Long lessonId){
        Lesson lesson = lessonRepository.findByIdAndTeacherId(lessonId, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        List<Lesson> lessonList = lessonRepository.findAllByChapterIdOrderByOrder(lesson.getChapter().getId());

        for (int i = 0; i < lessonList.size(); i++) {
            if (lessonList.get(i).getId().equals(lesson.getId())) {
                if (i < lessonList.size() - 1) {
                    Long nextOrder = lessonList.get(i + 1).getOrder();
                    lessonList.get(i).setOrder(nextOrder);
                    lessonList.get(i + 1).setOrder(lesson.getOrder());

                    lessonRepository.save(lessonList.get(i));
                    lessonRepository.save(lessonList.get(i + 1));
                }
                break;
            }
        }


        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update lesson order success");

        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        Lesson lesson = lessonRepository.findByIdAndTeacherId(id, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));

        lessonRepository.delete(lesson);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete lesson success");

        return apiMessageDto;
    }

    @PutMapping(value = "/move/{lessonId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> move(@PathVariable Long lessonId, @RequestParam Long newOrder){
        Lesson lesson = lessonRepository.findByIdAndTeacherId(lessonId, getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));

        Chapter chapter = lesson.getChapter();
        List<Lesson> lessons = chapter.getLessons();

        // get index from order
        int currentIndex = -1, newIndex = -1;
        for (int i = 0; i < chapter.getLessons().size(); i++) {
            Lesson l = chapter.getLessons().get(i);
            if(l.getOrder().equals(newOrder)){
                newIndex = i;
            }
            if(l.getOrder().equals(lesson.getOrder())){
                currentIndex = i;
            }
        }
        if(currentIndex < 0 || newIndex < 0){
            throw new NotFoundException("Move lesson failed");
        }

        // move lesson from current order to new order
        int start = Math.min(currentIndex, newIndex);
        int end = Math.max(currentIndex, newIndex);
        Long temp = 0L;
        for (int i = start; i <= end; i++) {
            if(lesson.getOrder() < newOrder) {
                // move up
                Long currentOrder = lessons.get(i).getOrder();
                lessons.get(i).setOrder(temp);
                temp = currentOrder;
            }else if (lesson.getOrder() > newOrder && i < lessons.size() - 1){
                // move down
                lessons.get(i).setOrder(lessons.get(i + 1).getOrder());
            }
        }
        lessons.get(currentIndex).setOrder(newOrder);

        chapterRepository.save(chapter);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Move lesson success");

        return apiMessageDto;
    }


    @GetMapping(value = "/client/lesson/{classId}/{lessonId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<LessonDto> clientGetLesson(@PathVariable Long classId, @PathVariable Long lessonId) {
        Class aClass;
        Lesson lesson;
        if (isTeacher()) {
            aClass = classRepository
                    .findByIdAndTeacherIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));
            lesson = lessonRepository.findById(lessonId).orElseThrow(()-> new NotFoundException("Lesson not found"));

        } else if (isStudent()) {
            aClass = classRepository
                    .findByIdAndStudentIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(()-> new NotFoundException("Class not found"));
            lesson = lessonRepository.findById(lessonId).orElseThrow(()-> new NotFoundException("Lesson not found"));
        } else {
            lesson = null;
        }

        if(lesson == null){
            throw new NotFoundException("Chapter not found");
        }

        ApiMessageDto<LessonDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(lessonMapper.fromEntityToDto(lesson));
        apiMessageDto.setMessage("Get lesson success");

        return apiMessageDto;
    }

    @GetMapping(value = "/client/chapter/{chapterId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<LessonDto>> clientGetByChapter(@PathVariable Long chapterId, Pageable pageable) {
        ApiMessageDto<ResponseListObj<LessonDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        ResponseListObj<LessonDto> responseListObj = new ResponseListObj<>();
        if(isStudent() || isTeacher()) {
            Chapter c = chapterRepository.findById(chapterId)
                    .orElseThrow(() -> new NotFoundException("Chapter not found"));
            Page<Lesson> listLesson = lessonRepository.findAllByChapterId(chapterId, pageable);

            responseListObj.setData(lessonMapper.fromEntitiesToDtos(listLesson.getContent()));
            responseListObj.setPage(pageable.getPageNumber());
            responseListObj.setTotalPage(listLesson.getTotalPages());
            responseListObj.setTotalElements(listLesson.getTotalElements());

            responseListObjApiMessageDto.setData(responseListObj);
            responseListObjApiMessageDto.setMessage("Get list success");
        } else {
            responseListObjApiMessageDto.setMessage("Get list failed");
        }
        return responseListObjApiMessageDto;
    }
}
