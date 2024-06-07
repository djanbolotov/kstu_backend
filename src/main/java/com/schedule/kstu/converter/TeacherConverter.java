package com.schedule.kstu.converter;

import com.schedule.kstu.model.Teacher;
import com.schedule.kstu.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TeacherConverter implements Converter<String, Teacher> {

    @Autowired
    private TeacherService teacherService;

    @Override
    public Teacher convert(String source) {
        Integer id = Integer.parseInt(source);
        return teacherService.getTeacherById(id);
    }
}
