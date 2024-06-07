package com.schedule.kstu.repository;

import com.schedule.kstu.model.Statement;
import com.schedule.kstu.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Integer> {
    @Query("SELECT s FROM Statement s JOIN s.students st WHERE st.id = :student")
    List<Statement> findByStudentsContains(@Param("student") Integer student);

    @Query("SELECT s FROM Statement s JOIN s.students st WHERE st.id = :student AND s.year = :year")
    List<Statement> findByStudentsContainsAndYear(@Param("student") Integer student, @Param("year") String year);

    @Query("SELECT s FROM Statement s JOIN s.students st WHERE st.id = :student AND s.semester = :semester")
    List<Statement> findByStudentsContainsAndSemester(@Param("student") Integer student, @Param("semester") String semester);

    @Query("SELECT s FROM Statement s JOIN s.students st WHERE st.id = :student AND s.year = :year AND s.semester = :semester")
    List<Statement> findByStudentsContainsAndYearAndSemester(@Param("student") Integer student, @Param("year") String year, @Param("semester") String semester);

    @Query("SELECT s FROM Statement s JOIN s.teacher te WHERE te.id = :teacher")
    List<Statement> findByTeacherContains(@Param("teacher") Integer teacher);

    @Query("SELECT s FROM Statement s JOIN s.teacher t WHERE t.id = :teacher AND s.year = :year")
    List<Statement> findByTeacherContainsAndYear(@Param("teacher") Integer teacher, @Param("year") String year);

    @Query("SELECT s FROM Statement s JOIN s.teacher t WHERE t.id = :teacher AND s.semester = :semester")
    List<Statement> findByTeacherContainsAndSemester(@Param("teacher") Integer teacher, @Param("semester") String semester);

    @Query("SELECT s FROM Statement s JOIN s.teacher t WHERE t.id = :teacher AND s.year = :year AND s.semester = :semester")
    List<Statement> findByTeacherContainsAndYearAndSemester(@Param("teacher") Integer teacher, @Param("year") String year, @Param("semester") String semester);
}
