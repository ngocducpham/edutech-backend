package com.edutech.api.controller;

import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.ErrorCode;
import com.edutech.api.dto.ResponseListObj;
import com.edutech.api.dto.news.NewsDto;
import com.edutech.api.form.news.CreateNewsForm;
import com.edutech.api.form.news.UpdateNewsForm;
import com.edutech.api.mapper.NewsMapper;
import com.edutech.api.service.EduTechApiService;
import com.edutech.api.storage.criteria.NewsCriteria;
import com.edutech.api.storage.model.Account;
import com.edutech.api.storage.model.News;
import com.edutech.api.storage.repository.NewsRepository;
import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.exception.RequestException;
import com.edutech.api.storage.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/news")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class NewsController extends ABasicController{

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    EduTechApiService eduTechApiService;

    @Autowired
    AccountRepository accountRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<NewsDto>> list(NewsCriteria newsCriteria, Pageable pageable) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.NEWS_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<ResponseListObj<NewsDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<News> list = newsRepository.findAll(newsCriteria.getSpecification(), pageable);
        ResponseListObj<NewsDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(newsMapper.fromEntityListToNewsDtoListNoNewsContent(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<NewsDto> get(@PathVariable("id") Long id){
        Account currentUser = accountRepository.findById(getCurrentUserId()) .orElse(null);
        if(currentUser == null
                || !currentUser.getKind().equals(EduTechConstant.USER_KIND_ADMIN)
                && !currentUser.getKind().equals(EduTechConstant.USER_KIND_EMPLOYEE)
                && !currentUser.getKind().equals(EduTechConstant.USER_KIND_COLLABORATOR)) {
            throw new RequestException(ErrorCode.NEWS_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<NewsDto> result = new ApiMessageDto<>();
        News news = newsRepository.findById(id).orElse(null);
        if(news == null){
            throw new RequestException(ErrorCode.NEWS_ERROR_NOT_FOUND);
        }
        if(!currentUser.getKind().equals(EduTechConstant.USER_KIND_ADMIN)
                && !news.getStatus().equals(EduTechConstant.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.NEWS_ERROR_NOT_FOUND);
        }

        result.setData(newsMapper.fromEntityToNewsDto(news));
        result.setMessage("Get news success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateNewsForm createNewsForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.NEWS_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        News news = newsMapper.fromCreateNewsFormToEntity(createNewsForm);

        newsRepository.save(news);
        apiMessageDto.setMessage("Create news success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateNewsForm updateNewsForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.NEWS_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        News news = newsRepository.findById(updateNewsForm.getId()).orElse(null);
        if (news == null) {
            throw new RequestException(ErrorCode.NEWS_ERROR_NOT_FOUND);
        }

        newsMapper.fromUpdateNewsFormToEntity(updateNewsForm, news);

        if (StringUtils.isNoneBlank(updateNewsForm.getAvatar())) {
            if(!updateNewsForm.getAvatar().equals(news.getAvatar())){
                //delete old image
                eduTechApiService.deleteFile(news.getAvatar());
            }
            news.setAvatar(updateNewsForm.getAvatar());
        }

        newsRepository.save(news);

        apiMessageDto.setMessage("Update news success");
        return apiMessageDto;

    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        if(!isAdmin()){
            throw new RequestException(ErrorCode.NEWS_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> result = new ApiMessageDto<>();
        News news = newsRepository.findById(id).orElse(null);
        if(news == null){
            throw new RequestException(ErrorCode.NEWS_ERROR_NOT_FOUND);
        }
        eduTechApiService.deleteFile(news.getAvatar());
        newsRepository.delete(news);
        result.setMessage("Delete success");
        return result;
    }

}