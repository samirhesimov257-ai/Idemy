package com.idemy.service;

import com.idemy.dao.entity.User;
import com.idemy.dao.repository.UserRepository;
import com.idemy.dto.OtpMessage;
import com.idemy.dto.request.LoginRequest;
import com.idemy.dto.request.RefreshTokenRequest;
import com.idemy.dto.request.RegisterRequest;
import com.idemy.dto.request.VerifyOtpRequest;
import com.idemy.dto.responce.AuthenticationResponse;
import com.idemy.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.idemy.service.messaging.OtpProducer;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final OtpService otpService;
    private final OtpProducer otpProducer;
    private final TokenBlacklistService tokenBlacklistService;

    public void register(RegisterRequest request) {

        var user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setEnabled(false);

        userRepository.save(user);

        String otp = otpService.generateAndStoreOtp(user.getEmail());
        otpProducer.sendOtpMessage(OtpMessage.builder()
                .email(user.getEmail())
                .otp(otp)
                .build());
    }

    public void verifyOtp(VerifyOtpRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();

        if (!otpService.isOtpValid(email, otp)) {
            throw new RuntimeException("OTP kodu səhvdir və ya vaxtı bitmişdir.");
        }

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + email));

        user.setEnabled(true);
        userRepository.save(user);

        // Başarılı doğrulamadan sonra kodu Redis'ten sil.
        otpService.deleteOtp(email);
    }

    public void resendOtp(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + email));

        // Yalnız təsdiqlənməmiş (disabled) hesablar resend edə bilər.
        if (user.isEnabled()) {
            throw new RuntimeException("Hesab artıq təsdiqlənib.");
        }

        if (otpService.hasResendLock(email)) {
            throw new RuntimeException("Yeni kod üçün zəhmət olmasa 60 saniyə gözləyin.");
        }

        String newOtp = otpService.generateAndStoreOtp(email);
        otpService.createResendLock(email);

        otpProducer.sendOtpMessage(OtpMessage.builder()
                .email(user.getEmail())
                .otp(newOtp)
                .build());
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
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null) {
            throw new RuntimeException("Refresh token etibarsızdır.");
        }
        UserDetails userDetails = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + userEmail));

        if (!jwtService.isTokenValid(refreshToken, userDetails) || tokenBlacklistService.isBlacklisted(refreshToken)) {
            throw new RuntimeException("Refresh token etibarsız və ya müddəti bitib.");
        }

        String newAccessToken = jwtService.generateAccessToken(userDetails);
        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String logout(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header düzgün göndərilməyib.");
        }
        String token = bearerToken.substring(7);
        Date expiration = jwtService.extractExpiration(token);
        tokenBlacklistService.blacklist(token, expiration.toInstant().isAfter(Instant.now()) ? expiration.toInstant() : Instant.now());
        return "Çıxış uğurla tamamlandı.";
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