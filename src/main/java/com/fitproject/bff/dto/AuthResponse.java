package com.fitproject.bff.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponse {
    private String userId;
    private String email;
    private String fullName;
    private String role;
    private Boolean active;
}