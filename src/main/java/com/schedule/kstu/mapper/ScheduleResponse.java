package com.schedule.kstu.mapper;

import com.schedule.kstu.DTO.ScheduleDTO;
import com.schedule.kstu.model.DaysOfWeek;

import java.util.List;

public class ScheduleResponse {
    private List<ScheduleDTO> schedules;
    private String selectedYear;
    private String selectedSemester;
    private List<DaysOfWeek> daysOfWeek;
    private List<String> years;
    public List<ScheduleDTO> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleDTO> schedules) {
        this.schedules = schedules;
    }
    public String getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
    }

    public String getSelectedSemester() {
        return selectedSemester;
    }

    public void setSelectedSemester(String selctedSemester) {
        this.selectedSemester = selctedSemester;
    }

    public List<DaysOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DaysOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }
}
