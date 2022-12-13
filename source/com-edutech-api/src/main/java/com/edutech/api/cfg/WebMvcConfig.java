package com.edutech.api.cfg;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.edutech.api.intercepter.MyIntercepter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@CrossOrigin
@EnableWebMvc
@EnableAsync
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    MyIntercepter myIntercepter;

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] checkArray = new String[]{
                "/v1/account/**",
                "/v1/group/**",
                "/v1/permission/**",
                "/v1/category/**",
                "/v1/news/**",
                "/v1/province/**",
                "/v1/major/**",
                "/v1/subject/**",
                "/v1/teacher/**",
                "/v1/teacher/subject/**",
                "/v1/student/**",
                "/v1/syllabus/**",
                "/v1/class/**",
                "/v1/lesson/**",
                "/v1/chapter/**",
                "/v1/assignment/**",
                "/v1/question/**",
                "/v1/resource/**",
                "/v1/classnews/**",
                "/v1/discuss/**",
                "/v1/comment/**",
                "/v1/class-assignment/**",
                "/v1/exam/**",
                "/v1/answer/**"
        };
        String[] arrayExclude = new String[]{"/v1/home/**"};
        registry.addInterceptor(myIntercepter).addPathPatterns(checkArray).excludePathPatterns(arrayExclude);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
        builder.dateFormat(new SimpleDateFormat(DATE_TIME_FORMAT));
        builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        builder.indentOutput(true);

        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
        converters.add(new MappingJackson2XmlHttpMessageConverter(builder.createXmlMapper(true).build()));
        converters.add(new ResourceHttpMessageConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateFormatter dateFormatter = new DateFormatter(DATE_TIME_FORMAT);
        dateFormatter.setLenient(true);
        registry.addFormatter(dateFormatter);
    }
}
