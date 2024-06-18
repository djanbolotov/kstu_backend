package com.schedule.kstu;

import com.schedule.kstu.DTO.AttendanceForCreateOrEditDTO;
import com.schedule.kstu.controllerREST.AttendanceRESTController;
import com.schedule.kstu.mapper.AttendanceRequest;
import com.schedule.kstu.mapper.AttendanceResponse;
import com.schedule.kstu.mapper.student.AttendanceStatisticResponse;
import com.schedule.kstu.mapper.teacher.AttendanceListForTeacherResponse;
import com.schedule.kstu.mapper.teacher.TeacherAttendanceStatisticResponse;
import com.schedule.kstu.model.*;
import com.schedule.kstu.service.*;
import com.schedule.kstu.service.auth.UserDetailsServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AttendanceRESTControllerTest {

    @Mock
    private AttendanceService attendanceService;

    @Mock
    private StatementService statementService;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserDetailsServiceImp userService;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private AttendanceRESTController attendanceRESTController;

    @Test
    public void testGetStudentAttendanceStatistics() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("studentUsername");

        User user = mock(User.class);
        Student student = mock(Student.class);
        when(user.getStudent()).thenReturn(student);
        when(userService.loadUserByUsername("studentUsername")).thenReturn(user);

        List<Statement> statements = List.of(mock(Statement.class));
        when(statementService.getStatementsForStudent(any(), any(), any())).thenReturn(statements);

        AttendanceStatisticResponse expectedResponse = new AttendanceStatisticResponse();

        ResponseEntity<AttendanceStatisticResponse> responseEntity = attendanceRESTController.getStudentAttendanceStatistics(principal, "2023-2024", "Fall Semester");

        assertNotNull(responseEntity);
        assertEquals(ResponseEntity.ok(expectedResponse).getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    public void testGetTeacherAttendanceStatistics() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("teacherUsername");

        User user = mock(User.class);
        Teacher teacher = mock(Teacher.class);
        when(user.getTeacher()).thenReturn(teacher);
        when(userService.loadUserByUsername("teacherUsername")).thenReturn(user);

        List<Statement> statements = List.of(mock(Statement.class));
        when(statementService.getStatementsForTeacher(any(), any(), any())).thenReturn(statements);

        TeacherAttendanceStatisticResponse expectedResponse = new TeacherAttendanceStatisticResponse();

        ResponseEntity<TeacherAttendanceStatisticResponse> responseEntity = attendanceRESTController.getTeacherAttendanceStatistics(principal, "2023-2024", "Fall Semester");

        assertNotNull(responseEntity);
        assertEquals(ResponseEntity.ok(expectedResponse).getStatusCode(), responseEntity.getStatusCode());
    }


//    @Test
//    public void testSaveAttendances() {
//        AttendanceRequest attendanceRequest = new AttendanceRequest();
//        attendanceRequest.setDate("2024-06-10T10:00:00");
//        attendanceRequest.setStatementId(1);
//        AttendanceForCreateOrEditDTO dto = new AttendanceForCreateOrEditDTO();
//        attendanceRequest.setAttendances(List.of(dto));
//
//        Statement statement = mock(Statement.class);
//        Student student = mock(Student.class);
//        when(statementService.getStatementById(1)).thenReturn(statement);
//        when(studentService.getStudentById(anyInt())).thenReturn(student);
//
//        ResponseEntity<AttendanceResponse> responseEntity = attendanceRESTController.saveAttendances(attendanceRequest);
//
//        assertNotNull(responseEntity);
//        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
//        verify(attendanceService, times(1)).createAttendances(anyList());
//    }

    @Test
    public void testDeleteAttendance() {
        List<Attendance> attendances = List.of(mock(Attendance.class));
        when(attendanceService.getAttendanceByStatementId(1)).thenReturn(attendances);

        ResponseEntity<String> responseEntity = attendanceRESTController.deleteAttendance(1, "2024-06-10T10:00:00");

        assertNotNull(responseEntity);
        assertEquals(ResponseEntity.ok("deleted").getStatusCode(), responseEntity.getStatusCode());
        verify(attendanceService, times(1)).deleteAttendance(anyInt());
    }

    @Test
    public void testGetStudentListForAttendance() {
        List<Student> students = List.of(mock(Student.class));
        when(studentService.getStudentsByStatement(anyInt())).thenReturn(students);

        ResponseEntity<List<AttendanceForCreateOrEditDTO>> responseEntity = attendanceRESTController.getStudentListForAttendance("group1", "1");

        assertNotNull(responseEntity);
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isEmpty());
    }

    @Test
    public void testGetTeacherAttendanceList() {
        // Мок Principal
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("teacherUsername");

        User user = mock(User.class);
        Teacher teacher = mock(Teacher.class);
        when(user.getTeacher()).thenReturn(teacher);
        when(userService.loadUserByUsername("teacherUsername")).thenReturn(user);

        List<Statement> statements = List.of(mock(Statement.class));
        when(statementService.getStatementsForTeacher(any(), any(), any())).thenReturn(statements);

        Discipline discipline = mock(Discipline.class);
        when(discipline.getName()).thenReturn("Math");
        when(statements.get(0).getDiscipline()).thenReturn(discipline);

        Student student = mock(Student.class);
        when(student.getGroup().getName()).thenReturn("Group1");
        when(statements.get(0).getStudents()).thenReturn(List.of(student));

        Attendance attendance = mock(Attendance.class);
        when(attendanceService.getAttendanceByStatementId(anyInt())).thenReturn(List.of(attendance));

        ResponseEntity<AttendanceListForTeacherResponse> responseEntity = attendanceRESTController.getTeacherAttendanceList(principal, "Math", "Group1", "2023-2024", "Fall Semester");

        assertNotNull(responseEntity);
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
    }

}
