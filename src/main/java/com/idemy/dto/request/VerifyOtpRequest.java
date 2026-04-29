package com.idemy.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpRequest {
    @Email
    @NotBlank
    private String email;

    // Kullanıcının girdiği OTP her zaman 4 haneli olmalı.
    @NotBlank
    @Pattern(regexp = "^\\d{4}$", message = "OTP 4 haneli sayısal kod olmalıdır.")
    private String otp;
}

