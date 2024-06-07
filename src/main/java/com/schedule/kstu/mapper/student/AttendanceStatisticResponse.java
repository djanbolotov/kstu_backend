package com.schedule.kstu.mapper.student;

import com.schedule.kstu.mapper.student.AttendancesWithDisciplines;

import java.util.List;

public class AttendanceStatisticResponse {
    private List<AttendancesWithDisciplines> attendancesWithDisciplines;
    private String selectedYear;
    private String selectedSemester;
    private List<String> years;
    public List<AttendancesWithDisciplines> getAttendancesWithDisciplines() {
        return attendancesWithDisciplines;
    }
    public void setAttendancesWithDisciplines(List<AttendancesWithDisciplines> attendancesWithDisciplines) {
        this.attendancesWithDisciplines = attendancesWithDisciplines;
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
