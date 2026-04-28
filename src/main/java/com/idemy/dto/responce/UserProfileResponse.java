package com.idemy.dto.responce;

import com.idemy.util.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private String fullName;
    private String email;
    private Role role;
    private String profileImageUrl;
}
