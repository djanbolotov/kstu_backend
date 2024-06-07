package com.schedule.kstu.repository;

import com.schedule.kstu.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.students WHERE g.id = :id")
    Group findGroupWithStudentsById(@Param("id") Integer id);

    @Query("SELECT g FROM Group g JOIN g.students st JOIN st.statements s WHERE s.id = :statementId")
    List<Group> findGroupsByStatementId(@Param("statementId") Integer statementId);
}
