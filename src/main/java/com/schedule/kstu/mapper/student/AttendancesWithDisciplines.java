package com.schedule.kstu.mapper.student;

import com.schedule.kstu.DTO.AttendanceDTO;
import com.schedule.kstu.DTO.DisciplineDTO;

import java.util.List;

public class AttendancesWithDisciplines {
    private DisciplineDTO discipline;
    private List<AttendanceDTO> attendances;
    private Integer countOfLessons;
    private Integer countOfAttendance;
    private Double percentageOfAttendance;
    public DisciplineDTO getDiscipline() {
        return discipline;
    }
    public void setDiscipline(DisciplineDTO discipline) {
        this.discipline = discipline;
    }
    public List<AttendanceDTO> getAttendances() {
        return attendances;
    }
    public void setAttendances(List<AttendanceDTO> attendances) {
        this.attendances = attendances;
    }

    public Integer getCountOfLessons() {
        return countOfLessons;
    }

    public void setCountOfLessons(Integer countOfLessons) {
        this.countOfLessons = countOfLessons;
    }

    public Integer getCountOfAttendance() {
        return countOfAttendance;
    }

    public void setCountOfAttendance(Integer countOfAttendance) {
        this.countOfAttendance = countOfAttendance;
    }

    public Double getPercentageOfAttendance() {
        return percentageOfAttendance;
    }

    public void setPercentageOfAttendance(Double percentageOfAttendance) {
        this.percentageOfAttendance = percentageOfAttendance;
    }
}
