package com.idemy.dto.request;

import com.idemy.util.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Qeydiyyat üçün
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private Role role; // STUDENT və ya INSTRUCTOR
}

