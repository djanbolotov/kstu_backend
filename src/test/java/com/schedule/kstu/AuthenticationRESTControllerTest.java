package com.schedule.kstu;

import com.schedule.kstu.controllerREST.AuthenticationRESTController;
import com.schedule.kstu.mapper.AuthenticationResponse;
import com.schedule.kstu.model.Role;
import com.schedule.kstu.model.User;
import com.schedule.kstu.service.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class AuthenticationRESTControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private AuthenticationRESTController authenticationRESTController;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationRESTController).build();
    }
    @Test
    public void testLogin() throws Exception {
        User user = new User();
        user.setUsername("djanbolotov16");
        user.setPassword("askabek2003");

        AuthenticationResponse response = new AuthenticationResponse("dummy-token", Role.STUDENT);

        when(authenticationService.authenticate(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"djanbolotov16\", \"password\": \"askabek2003\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"dummy-token\"}"));
    }

    @Test
    public void testLoginTrue() throws Exception {
        User user = new User();
        user.setUsername("djanbolotov16");
        user.setPassword("askabek2003");

        AuthenticationResponse response = new AuthenticationResponse("dummy-token", Role.STUDENT);

        when(authenticationService.authenticate(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"djanbolotov16\", \"password\": \"askabek2003\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"dummy-token\"}"));
    }

    @Test
    public void testLoginEmpty() throws Exception {
        User user = new User();
        user.setUsername("djanbolotovTEST");
        user.setPassword("");

        AuthenticationResponse response = new AuthenticationResponse("dummy-token", Role.STUDENT);

        when(authenticationService.authenticate(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"djanbolotovTEST\", \"password\": \"\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"dummy-token\"}"));
    }

    @Test
    public void testRegister() throws Exception {
        User user = new User("Askabek", "Djanbolotov", "djanbolotov16", "askabek2003", Role.STUDENT);

        AuthenticationResponse response = new AuthenticationResponse("dummy-token", Role.STUDENT);

        when(authenticationService.register(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Askabek\", \"lastName\": \"Djanbolotov\", \"username\": \"djanbolotov16\", \"password\": \"askabek2003\", \"role\": \"STUDENT\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"dummy-token\"}"));
    }
    @Test
    public void testRegisterSuccess() throws Exception {
        User user = new User("Nurlan", "Nurdinov", "nuni", "nuni2002", Role.STUDENT);

        AuthenticationResponse response = new AuthenticationResponse("dummy-token", Role.STUDENT);

        when(authenticationService.register(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Nurlan\", \"lastName\": \"Nurdinov\", \"username\": \"nuni\", \"password\": \"nuni2002\", \"role\": \"STUDENT\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"dummy-token\"}"));
    }

    @Test
    public void testRegisterFailure() throws Exception {
        User user = new User("Nurlan", "Nurdinov", "nuni", "Nurlan", Role.STUDENT);

        AuthenticationResponse response = new AuthenticationResponse("dummy-token", Role.STUDENT);

        when(authenticationService.register(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Nurlan\", \"lastName\": \"Nurdinov\", \"username\": \"nuni\", \"password\": \"Nurlan\", \"role\": \"STUDENT\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"dummy-token\"}"));
    }
}
