package com.fitproject.bff.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreateUserRequest {
    private String email;
    private String password;
    private String fullName;
    private String role;
}