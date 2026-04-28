package com.idemy.controller;

import com.idemy.dao.entity.User;
import com.idemy.dao.repository.UserRepository;
import com.idemy.dto.responce.UserProfileResponse;
import com.idemy.mapper.UserMapper;
import com.idemy.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper ;
    private final UserRepository userRepository;
    private final FileService fileService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(userMapper.mapToProfileResponse(user));
    }

    @PostMapping("/profile-image")
    public ResponseEntity<UserProfileResponse> uploadProfileImage(
            Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        String imageKey = fileService.saveFile(file, "profile-images");
        user.setProfileImageUrl(imageKey);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.mapToProfileResponse(user));
    }


}
