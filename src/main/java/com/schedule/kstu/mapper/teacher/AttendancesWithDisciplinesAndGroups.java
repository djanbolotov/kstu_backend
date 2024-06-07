package com.schedule.kstu.mapper.teacher;

import com.schedule.kstu.DTO.AttendanceDTO;
import com.schedule.kstu.DTO.AttendanceForTeacherDTO;
import com.schedule.kstu.DTO.DisciplineDTO;

import java.util.List;

public class AttendancesWithDisciplinesAndGroups {
    private DisciplineDTO discipline;
    private List<AttendanceForTeacherDTO> attendances;
    private Integer statementId;
    private String groupName;
    private Integer countOfLessons;
    private Integer countOfAttendance;
    private Double percentageOfAttendance;
    public DisciplineDTO getDiscipline() {
        return discipline;
    }
    public void setDiscipline(DisciplineDTO discipline) {
        this.discipline = discipline;
    }
    public List<AttendanceForTeacherDTO> getAttendances() {
        return attendances;
    }
    public void setAttendances(List<AttendanceForTeacherDTO> attendances) {
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

    public String getGroupName() {
        return groupName;
    }

    public Integer getStatementId() {
        return statementId;
    }

    public void setStatementId(Integer statementId) {
        this.statementId = statementId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setPercentageOfAttendance(Double percentageOfAttendance) {
        this.percentageOfAttendance = percentageOfAttendance;
    }
}
