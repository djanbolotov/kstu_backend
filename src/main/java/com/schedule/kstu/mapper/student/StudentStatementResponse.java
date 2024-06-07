package com.schedule.kstu.mapper.student;

import com.schedule.kstu.DTO.DisciplineDTO;

import java.util.List;

public class StudentStatementResponse {
    private String firstName;
    private String lastName;
    private String group;
    private String selectedYear;
    private String selectedSemester;

    public String getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
    }

    public String getSelectedSemester() {
        return selectedSemester;
    }

    public void setSelectedSemester(String selectedSemester) {
        this.selectedSemester = selectedSemester;
    }

    private List<String> years;
    private List<DisciplineDTO> disciplines;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public void setDisciplines(List<DisciplineDTO> disciplines) {
        this.disciplines = disciplines;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGroup() {
        return group;
    }

    public List<String> getYears() {
        return years;
    }

    public List<DisciplineDTO> getDisciplines() {
        return disciplines;
    }
}
