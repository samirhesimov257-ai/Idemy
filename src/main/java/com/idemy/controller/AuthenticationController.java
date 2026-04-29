

package com.idemy.controller;

import com.idemy.dto.request.LoginRequest;
import com.idemy.dto.request.RegisterRequest;
import com.idemy.dto.request.VerifyOtpRequest;
import com.idemy.dto.responce.AuthenticationResponse;
import com.idemy.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        service.register(request);
        return ResponseEntity.ok("Istifadəçi gözləmədədir. OTP e-maile  gönderildi.");

    }
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest request) {
        service.verifyOtp(request);
        return ResponseEntity.ok("OTP doğrulandı. Hesabınız aktif edildi.");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {
        service.resendOtp(email);
        return ResponseEntity.ok("Yeni OTP kodu e-maile göndərildi.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}