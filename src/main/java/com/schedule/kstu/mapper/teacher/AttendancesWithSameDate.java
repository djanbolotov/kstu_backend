package com.schedule.kstu.mapper.teacher;

import com.schedule.kstu.DTO.AttendanceForCreateOrEditDTO;

import java.util.List;

public class AttendancesWithSameDate {
    private String date;
    private List<AttendanceForCreateOrEditDTO> attendances;

    public AttendancesWithSameDate(String date, List<AttendanceForCreateOrEditDTO> attendances) {
        this.date = date;
        this.attendances = attendances;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<AttendanceForCreateOrEditDTO> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<AttendanceForCreateOrEditDTO> attendances) {
        this.attendances = attendances;
    }
}
