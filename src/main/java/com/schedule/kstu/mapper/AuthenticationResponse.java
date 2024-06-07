package com.schedule.kstu.mapper;

import com.schedule.kstu.model.Role;

public class AuthenticationResponse {

    private String token;

    private Role role;

    public AuthenticationResponse(String token, Role role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public Role getRole() {
        return role;
    }
}
