package com.schedule.kstu.mapper.teacher;

import com.schedule.kstu.DTO.AttendanceForCreateOrEditDTO;
import com.schedule.kstu.DTO.DisciplineDTO;
import com.schedule.kstu.DTO.GroupDTO;
import com.schedule.kstu.mapper.student.AttendancesWithDisciplines;

import java.util.List;

public class AttendanceListForTeacherResponse {
    private List<AttendancesWithSameDate> attendancesWithSameDates;
    private String selectedYear;
    private String selectedSemester;

    private Integer statementId;
    private String selectedDiscipline;
    private String selectedGroup;
    private List<String> years;
    private List<String> groups;

    private List<String> disciplines;
    public List<AttendancesWithSameDate> getAttendancesWithSameDates() {
        return attendancesWithSameDates;
    }
    public void setAttendancesWithSameDates(List<AttendancesWithSameDate> attendancesWithSameDates) {
        this.attendancesWithSameDates = attendancesWithSameDates;
    }

    public Integer getStatementId() {
        return statementId;
    }

    public void setStatementId(Integer statementId) {
        this.statementId = statementId;
    }

    public String getSelectedDiscipline() {
        return selectedDiscipline;
    }

    public void setSelectedDiscipline(String selectedDiscipline) {
        this.selectedDiscipline = selectedDiscipline;
    }

    public String getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(String selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<String> disciplines) {
        this.disciplines = disciplines;
    }

    public String getSelectedYear() { return selectedYear; }
    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
    }
    public String getSelectedSemester() {
        return selectedSemester;
    }
    public void setSelectedSemester(String selectedSemester) {
        this.selectedSemester = selectedSemester;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public List<String> getYears() {
        return years;
    }
}
