package com.schedule.kstu.DTO;

import com.schedule.kstu.model.AttendanceSTATUS;

import java.time.LocalDateTime;

public class AttendanceForTeacherDTO {
    private Integer id;
    private AttendanceSTATUS attendanceStatus;
    private String studentFullName;
    private LocalDateTime attendanceDate;

    public AttendanceForTeacherDTO(Integer id, AttendanceSTATUS attendanceStatus, String studentFullName, LocalDateTime attendanceDate) {
        this.id = id;
        this.attendanceStatus = attendanceStatus;
        this.studentFullName = studentFullName;
        this.attendanceDate = attendanceDate;
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

    public String getStudentFullName() {
        return studentFullName;
    }

    public void setStudentFullName(String studentFullName) {
        this.studentFullName = studentFullName;
    }
}
