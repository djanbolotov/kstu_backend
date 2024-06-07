package com.schedule.kstu.converter;

import com.schedule.kstu.model.Discipline;
import com.schedule.kstu.service.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DisciplineConverter implements Converter<String, Discipline> {

    @Autowired
    private DisciplineService disciplineService;

    @Override
    public Discipline convert(String source) {
        Integer id = Integer.parseInt(source);
        return disciplineService.getDisciplineById(id);
    }
}
