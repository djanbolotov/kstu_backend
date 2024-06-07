package com.schedule.kstu.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "statement_id", referencedColumnName = "id")
    private Statement statement;

    @Enumerated(value = EnumType.STRING)
    private AttendanceSTATUS attendanceStatus;

    @Column(name = "attendance_date")
    private LocalDateTime attendanceDate;

    // Constructors
    public Attendance() {}

    public Attendance(Student student, Statement statement, AttendanceSTATUS attendanceStatus, LocalDateTime attendanceDate) {
        this.student = student;
        this.statement = statement;
        this.attendanceStatus = attendanceStatus;
        this.attendanceDate = attendanceDate;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
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
