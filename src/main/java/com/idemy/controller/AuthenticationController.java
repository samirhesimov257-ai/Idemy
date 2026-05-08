

package com.idemy.controller;

import com.idemy.dto.request.LoginRequest;
import com.idemy.dto.request.RefreshTokenRequest;
import com.idemy.dto.request.RegisterRequest;
import com.idemy.dto.request.VerifyOtpRequest;
import com.idemy.dto.responce.AuthenticationResponse;
import com.idemy.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(summary = "Registirasiya əməliyyatlarını yerinə yetirmək üçün")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        service.register(request);
        return ResponseEntity.ok("Istifadəçi gözləmədədir. OTP e-maile  gönderildi.");

    }
    @Operation(summary = "OTP nin doğrulanması")
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest request) {
        service.verifyOtp(request);
        return ResponseEntity.ok("OTP doğrulandı. Hesabınız aktif edildi.");
    }
    @Operation(summary = "Yeni OTP göndərmək üçün")
    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {
        service.resendOtp(email);
        return ResponseEntity.ok("Yeni OTP kodu e-maile göndərildi.");
    }
    @Operation(summary = "Login olmaq üçün")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
    @Operation(summary = "Access token un yenilənməsi")
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(service.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(service.logout(authorizationHeader));
    }
}