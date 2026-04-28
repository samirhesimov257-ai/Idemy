package com.idemy.mapper;

import com.idemy.dao.entity.User;
import com.idemy.dto.responce.UserProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserProfileResponse mapToProfileResponse(User user) {
        return UserProfileResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
