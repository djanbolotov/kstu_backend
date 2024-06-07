package com.schedule.kstu.DTO;

import com.schedule.kstu.model.AttendanceSTATUS;

import java.time.LocalDateTime;

public class AttendanceForCreateOrEditDTO {
    private Integer id;
    private AttendanceSTATUS attendanceStatus;
    private Integer studentId;
    private String studentFullName;
    private Integer studentSubgroup;
    private LocalDateTime attendanceDate;

    public AttendanceForCreateOrEditDTO(Integer id, AttendanceSTATUS attendanceStatus, Integer studentId, String studentFullName, Integer studentSubgroup, LocalDateTime attendanceDate) {
        this.id = id;
        this.attendanceStatus = attendanceStatus;
        this.studentId = studentId;
        this.studentFullName = studentFullName;
        this.studentSubgroup = studentSubgroup;
        this.attendanceDate = attendanceDate;
    }

    public Integer getStudentSubgroup() {
        return studentSubgroup;
    }

    public void setStudentSubgroup(Integer studentSubgroup) {
        this.studentSubgroup = studentSubgroup;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AttendanceSTATUS getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceSTATUS attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public LocalDateTime getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDateTime attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentFullName() {
        return studentFullName;
    }

    public void setStudentFullName(String studentFullName) {
        this.studentFullName = studentFullName;
    }
}
