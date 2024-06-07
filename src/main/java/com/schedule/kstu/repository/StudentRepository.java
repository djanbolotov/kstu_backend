package com.schedule.kstu.repository;

import com.schedule.kstu.model.Statement;
import com.schedule.kstu.model.Student;
import com.schedule.kstu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findStudentByUserId(Integer id);

    List<Student> findStudentsByGroupId(Integer id);
    @Query("SELECT s FROM Student s JOIN s.statements st WHERE st.id = :id")
    List<Student> findStudentsByStatementsContainingAndId(@Param("id") Integer id);

//    @Query("SELECT s FROM Student s JOIN s.statements st WHERE st.id = :id AND s.group.name = :group")
//    List<Student> findStudentsByGroupAndStatementId(@Param("group") String group, @Param("statementId") Integer statementId);
//
//    @Query("SELECT s FROM Student s JOIN s.statements st WHERE st.id = :id AND s.group.name = :group AND s.subgroup = :subgroup")
//    List<Student> findStudentsByGroupAndSubroupAndStatementId(@Param("group") String group, @Param("subgroup") Integer subgroup, @Param("statementId") Integer statementId);

}
