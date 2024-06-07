package com.schedule.kstu.mapper;

import com.schedule.kstu.DTO.AttendanceForCreateOrEditDTO;

import java.util.List;

public class AttendanceRequest {
    private Integer statementId;
    private String date;
    private List<AttendanceForCreateOrEditDTO> attendances;

    public AttendanceRequest(Integer statementId, String date, List<AttendanceForCreateOrEditDTO> attendances) {
        this.statementId = statementId;
        this.date = date;
        this.attendances = attendances;
    }

    public Integer getStatementId() {
        return statementId;
    }

    public void setStatementId(Integer statementId) {
        this.statementId = statementId;
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
