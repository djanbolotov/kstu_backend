package com.schedule.kstu.repository;

import com.schedule.kstu.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    @Query("SELECT a FROM Attendance a WHERE a.student.id = :student AND a.statement.id = :statement")
    List<Attendance> findAttendanceByStudentAndStatement(@Param("student") Integer student, @Param("statement") Integer statement);

    @Query("SELECT a FROM Attendance a WHERE a.statement.id = :statement")
    List<Attendance> findAttendanceByStatement(@Param("statement") Integer statement);

}
