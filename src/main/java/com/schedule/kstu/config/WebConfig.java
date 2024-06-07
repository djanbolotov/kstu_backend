package com.schedule.kstu.config;

import com.schedule.kstu.converter.DisciplineConverter;
import com.schedule.kstu.converter.TeacherConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private DisciplineConverter disciplineConverter;

    @Autowired
    private TeacherConverter teacherConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(disciplineConverter);
        registry.addConverter(teacherConverter);
    }
}
