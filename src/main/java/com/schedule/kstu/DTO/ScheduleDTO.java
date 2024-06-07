package com.schedule.kstu.DTO;

import com.schedule.kstu.model.DaysOfWeek;

public class ScheduleDTO {
    private Integer id;
    private String teacher;
    private String startTime;
    private String endTime;
    private DaysOfWeek dayOfWeek;
    private DisciplineDTO discipline;
    private String group;

    public ScheduleDTO(Integer id, String teacher, String startTime, String endTime, DisciplineDTO discipline, String group, DaysOfWeek dayOfWeek) {
        this.id = id;
        this.teacher = teacher;
        this.startTime = startTime;
        this.endTime = endTime;
        this.discipline = discipline;
        this.group = group;
        this.dayOfWeek = dayOfWeek;
    }

    public DaysOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DaysOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public DisciplineDTO getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineDTO discipline) {
        this.discipline = discipline;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}

