package com.edutech.api.controller;

import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ErrorCode;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.discusscomment.DiscussCommentDto;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.exception.RequestException;
import com.edutech.api.form.discusscomment.CreateDiscussCommentForm;
import com.edutech.api.form.discusscomment.UpdateDiscussCommentForm;
import com.edutech.api.mapper.DiscussCommentMapper;
import com.edutech.api.storage.criteria.DiscussCommentCriteria;
import com.edutech.api.storage.model.*;
import com.edutech.api.storage.repository.AccountRepository;
import com.edutech.api.storage.repository.DiscussCommentRepository;
import com.edutech.api.storage.repository.LessonDiscussRepository;
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
@RequestMapping("/v1/comment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class DiscussCommentController extends ABasicController{
    @Autowired
    private DiscussCommentRepository repository;

    @Autowired
    private DiscussCommentMapper commentmapper;

    @Autowired
    private LessonDiscussRepository discussRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<DiscussCommentDto>> getList (@Valid DiscussCommentCriteria criteria, Pageable pageable, BindingResult result) {
        ApiMessageDto<ResponseListObj<DiscussCommentDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<DiscussComment> listdiscuss = repository.findAll(criteria.getSpecification(), pageable);
        ResponseListObj<DiscussCommentDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(commentmapper.fromEntitiesToDtos(listdiscuss.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listdiscuss.getTotalPages());
        responseListObj.setTotalElements(listdiscuss.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<DiscussCommentDto> get(@PathVariable Long commentId) {
        DiscussComment discuss = repository.findById(commentId)
                .orElseThrow(()-> new NotFoundException("Comment not found"));
        ApiMessageDto<DiscussCommentDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(commentmapper.fromEntityToDto(discuss));
        apiMessageDto.setMessage("Get comment success");

        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateDiscussCommentForm form, BindingResult result) {
        discussRepository.findById(form.getDiscussId())
                .orElseThrow(()-> new NotFoundException("Discuss not found"));
        accountRepository.findById(form.getUserId())
                .orElseThrow(()-> new NotFoundException("User not found"));
        if(form.getParentId()!=null){
            repository.findById(form.getParentId())
                    .orElseThrow(()-> new NotFoundException("Parent not found"));
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        form.setCreated_date(new Date());
        DiscussComment comment = commentmapper.fromCreateFormToEntity(form);

        if(form.getParentId() != null) {
            DiscussComment parentcmt = repository.findById(form.getParentId()).orElse(null);
            if(parentcmt == null || parentcmt.getParentComment() != null) {
                throw new RequestException(ErrorCode.COMMENT_ERROR_NOT_FOUND, "Not found comment parent");
            }
            comment.setParentComment(parentcmt);
        } else {
            comment.setParentComment(null);
        }

        repository.save(comment);

        apiMessageDto.setMessage("Create comment successfully");

        return apiMessageDto;
    }

    @PutMapping(value = "/update/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateDiscussCommentForm form, @PathVariable Long commentId, BindingResult result){
        DiscussComment comment = repository.findById(commentId)
                .orElseThrow(()-> new NotFoundException("Comment not found"));
        commentmapper.fromUpdateFormToEntity(form, comment);
        comment = repository.save(comment);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Update comment success");

        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        DiscussComment comment = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Comment not found"));

        repository.delete(comment);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete comment success");

        return apiMessageDto;
    }
    @GetMapping(value = "/client/get/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<DiscussCommentDto> clientGet(@PathVariable Long commentId) {
        ApiMessageDto<DiscussCommentDto> apiMessageDto = new ApiMessageDto<>();
        DiscussComment comment = repository.findById(commentId)
                .orElseThrow(()-> new NotFoundException("Discuss not found"));

        apiMessageDto.setData(commentmapper.fromEntityToDto(comment));
        apiMessageDto.setMessage("Client get comment success");
        return apiMessageDto;
    }

    @GetMapping(value = "/client/get/comment-list/{discussId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<DiscussCommentDto>> clientGetCommentList (@Valid DiscussCommentCriteria criteria, @PathVariable Long discussId, Pageable pageable, BindingResult result) {

        ApiMessageDto<ResponseListObj<DiscussCommentDto>> apiMessageDto = new ApiMessageDto<>();

        criteria.setUserId(getCurrentUserId());

        List<DiscussComment> page = repository.listAllByDiscussId(discussId);

        ResponseListObj<DiscussCommentDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(commentmapper.fromEntitiesToDtos(page));

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("Client get discuss comment list success");

        return apiMessageDto;
    }

    @GetMapping(value = "/client/get/child-comment-list/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<DiscussCommentDto>> clientGetChildCommentList (@Valid DiscussCommentCriteria criteria, @PathVariable Long commentId, Pageable pageable, BindingResult result) {

        ApiMessageDto<ResponseListObj<DiscussCommentDto>> apiMessageDto = new ApiMessageDto<>();

        criteria.setUserId(getCurrentUserId());

        Page<DiscussComment> page = repository.findAllByParentId(commentId, pageable);

        ResponseListObj<DiscussCommentDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(commentmapper.fromEntitiesToDtos(page.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(page.getTotalPages());
        responseListObj.setTotalElements(page.getTotalElements());

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("Client get comment child list success");

        return apiMessageDto;
    }
    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> clientCreate(@Valid @RequestBody CreateDiscussCommentForm form, BindingResult result) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        LessonDiscuss discuss = discussRepository.findById(form.getDiscussId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        Account user = accountRepository.findById(getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("User not found"));


        form.setUserId(getCurrentUserId());
        form.setCreated_date(new Date());
        DiscussComment comment = commentmapper.fromCreateFormToEntity(form);

        if(form.getParentId() != null) {
            DiscussComment parentcmt = repository.findById(form.getParentId()).orElse(null);
            if(parentcmt == null || parentcmt.getParentComment() != null) {
                throw new RequestException(ErrorCode.COMMENT_ERROR_NOT_FOUND, "Not found comment parent");
            }
            comment.setParentComment(parentcmt);
        } else {
            comment.setParentComment(null);
        }
        repository.save(comment);

        apiMessageDto.setMessage("Client comment success");
        return apiMessageDto;
    }

    @PutMapping(value = "/client/update/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> clientUpdate(@Valid @RequestBody UpdateDiscussCommentForm form, @PathVariable Long commentId, BindingResult result){
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        DiscussComment comment = repository.findById(commentId)
                .orElseThrow(()-> new NotFoundException("Discuss not found"));
        Account user = accountRepository.findById(getCurrentUserId())
                .orElseThrow(()-> new NotFoundException("User not found"));

        commentmapper.fromUpdateFormToEntity(form, comment);
        if(comment.getUser().getId() != getCurrentUserId()){
            apiMessageDto.setMessage("Client update comment failed, userId incorrect");
            return apiMessageDto;
        } else {
            repository.save(comment);

            apiMessageDto.setMessage("Client update comment success");

            return apiMessageDto;
        }
    }

    @DeleteMapping(value = "/client/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> clientDelete(@PathVariable("id") Long id){
        DiscussComment comment = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Comment not found"));
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        if(getCurrentUserId() != comment.getUser().getId()){
            apiMessageDto.setMessage("Delete comment failed, userID incorrect");
            return apiMessageDto;
        } else {
            repository.delete(comment);
            apiMessageDto.setMessage("Delete comment success");
            return apiMessageDto;
        }
    }
}
