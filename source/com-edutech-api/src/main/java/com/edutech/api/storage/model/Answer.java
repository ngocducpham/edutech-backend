package com.edutech.api.storage.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Answer {
    private List<Content> contents;
    private final static ObjectMapper mapper = new ObjectMapper();

    public Answer() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }

    public static Answer parseJson(String json) throws JsonProcessingException {
        return mapper.readValue(json, Answer.class);
    }

    @Getter
    @Setter
    public static class Content{
        private Long id;
        private String content;
        private Boolean rightAnswer;
    }
}