package com.idemy.service;

import com.idemy.dao.entity.User;
import com.idemy.dao.repository.UserRepository;
import com.idemy.dto.request.LoginRequest;
import com.idemy.dto.request.RegisterRequest;
import com.idemy.dto.responce.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {
        // 1. Yeni istifadəçi obyekti yarat
        var user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Şifrəni gizlət
        user.setRole(request.getRole());

        // 2. Bazaya yaz
        userRepository.save(user);

        // 3. Token yarat və qaytar
//        return jwtService.generateToken(user);
    }
    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Refresh token-i bazaya da yadda saxla (yeni metod yazmalısan bunun üçün)
//        saveRefreshToken(user, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken+"\n")
                .refreshToken(refreshToken)

                .build();
    }
//    public String login(LoginRequest request) {
//        // 1. Email və şifrəni yoxla (Düzgün deyilsə Exception atacaq)
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
//        // 2. İstifadəçini bazadan tap
//        var user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));
//
//        // 3. Yeni token yarat və qaytar
//        return jwtService.generateToken(user);
//    }
}