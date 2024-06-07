package com.schedule.kstu.service;

import com.schedule.kstu.model.Attendance;
import com.schedule.kstu.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    public Optional<Attendance> getAttendanceById(Integer id) {
        return attendanceRepository.findById(id);
    }

    public Attendance createAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> createAttendances(List<Attendance> attendances) {
        return attendanceRepository.saveAll(attendances);
    }

    public List<Attendance> getAttendanceByStudentIdAndStatementId(Integer student, Integer statement) {
        return attendanceRepository.findAttendanceByStudentAndStatement(student, statement);
    }

    public List<Attendance> getAttendanceByStatementId(Integer statement) {
        return attendanceRepository.findAttendanceByStatement(statement);
    }

    public Attendance updateAttendance(Integer id, Attendance attendance) {
        Optional<Attendance> existingAttendance = attendanceRepository.findById(id);
        if (existingAttendance.isPresent()) {
            Attendance updatedAttendance = existingAttendance.get();
            updatedAttendance.setStudent(attendance.getStudent());
            updatedAttendance.setStatement(attendance.getStatement());
            updatedAttendance.setAttendanceStatus(attendance.getAttendanceStatus());
            updatedAttendance.setAttendanceDate(attendance.getAttendanceDate());
            return attendanceRepository.save(updatedAttendance);
        } else {
            throw new RuntimeException("Attendance not found with id " + id);
        }
    }

    public void deleteAttendance(Integer id) {
        attendanceRepository.deleteById(id);
    }
}
