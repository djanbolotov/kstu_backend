package com.schedule.kstu.repository;

import com.schedule.kstu.model.Schedule;
import com.schedule.kstu.model.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByStatementId(Integer statementId);

    @Query("SELECT s FROM Schedule s " +
            "JOIN s.statement st " +
            "WHERE st.teacher.id = :teacherId " +
            "AND st.year = :year " +
            "AND st.semester = :semester")
    List<Schedule> findSchedulesByTeacherIdAndYearAndSemester(
            @Param("teacherId") Integer teacherId,
            @Param("year") String year,
            @Param("semester") String semester
    );

    @Query("SELECT s FROM Schedule s " +
            "JOIN s.statement st " +
            "WHERE st.teacher.id = :teacherId " +
            "AND st.year = :year " )
    List<Schedule> findSchedulesByTeacherIdAndYear(
            @Param("teacherId") Integer teacherId,
            @Param("year") String year
    );

    @Query("SELECT s FROM Schedule s " +
            "JOIN s.statement st " +
            "WHERE st.teacher.id = :teacherId " +
            "AND st.semester = :semester")
    List<Schedule> findSchedulesByTeacherIdAndSemester(
            @Param("teacherId") Integer teacherId,
            @Param("semester") String semester
    );

    @Query("SELECT s FROM Schedule s " +
            "JOIN s.statement st " +
            "WHERE st.teacher.id = :teacherId ")
    List<Schedule> findSchedulesByTeacherId(
            @Param("teacherId") Integer teacherId
    );
    @Query("SELECT s FROM Schedule s " +
            "JOIN s.statement st " +
            "JOIN st.students stus " +
            "WHERE stus.id = :studentId " +
            "AND st.year = :year " +
            "AND st.semester = :semester")
    List<Schedule> findSchedulesByStudentIdAndYearAndSemester(
            @Param("studentId") Integer studentId,
            @Param("year") String year,
            @Param("semester") String semester
    );

    @Query("SELECT s FROM Schedule s " +
            "JOIN s.statement st " +
            "JOIN st.students stus " +
            "WHERE stus.id = :studentId " +
            "AND st.year = :year ")
    List<Schedule> findSchedulesByStudentIdAndYear(
            @Param("studentId") Integer studentId,
            @Param("year") String year
    );

    @Query("SELECT s FROM Schedule s " +
            "JOIN s.statement st " +
            "JOIN st.students stus " +
            "WHERE stus.id = :studentId " +
            "AND st.semester = :semester")
    List<Schedule> findSchedulesByStudentIdAndSemester(
            @Param("studentId") Integer studentId,
            @Param("semester") String semester
    );

    @Query("SELECT s FROM Schedule s " +
            "JOIN s.statement st " +
            "JOIN st.students stus " +
            "WHERE stus.id = :studentId ")
    List<Schedule> findSchedulesByStudentId(
            @Param("studentId") Integer studentId
    );
}
