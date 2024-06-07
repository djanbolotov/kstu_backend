package com.schedule.kstu.service.auth;

import com.schedule.kstu.mapper.AuthenticationResponse;
import com.schedule.kstu.model.Student;
import com.schedule.kstu.model.Teacher;
import com.schedule.kstu.model.User;
import com.schedule.kstu.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        switch (request.getRole()){
            case STUDENT -> {
                Student student = new Student();
                student.setUser(user);
                user.setStudent(student);
            }
            case TEACHER -> {
                Teacher teacher = new Teacher();
                teacher.setUser(user);
                user.setTeacher(teacher);
            }
            default -> {

            }
        }

        user = repository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token, user.getRole());
    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token, user.getRole());
    }

    public List<User> getAllUsers() {
        List<User> userList = repository.findAll();
        return userList;
    }
}
