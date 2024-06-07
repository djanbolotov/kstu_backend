package com.schedule.kstu.mapper;

import com.schedule.kstu.DTO.AttendanceDTO;

import java.util.List;

public class AttendanceResponse {
    private List<AttendanceDTO> attendances;

    public List<AttendanceDTO> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<AttendanceDTO> attendances) {
        this.attendances = attendances;
    }
}
