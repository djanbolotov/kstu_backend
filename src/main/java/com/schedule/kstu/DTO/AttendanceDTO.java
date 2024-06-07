package com.schedule.kstu.DTO;

import com.schedule.kstu.model.AttendanceSTATUS;

import java.time.LocalDateTime;

public class AttendanceDTO {
    private Integer id;
    private AttendanceSTATUS attendanceStatus;

    private LocalDateTime attendanceDate;

    public AttendanceDTO(Integer id, AttendanceSTATUS attendanceStatus, LocalDateTime attendanceDate) {
        this.id = id;
        this.attendanceStatus = attendanceStatus;
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

   }
