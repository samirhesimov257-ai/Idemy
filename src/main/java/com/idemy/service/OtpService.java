package com.idemy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OtpService {

    private static final Duration OTP_TTL = Duration.ofMinutes(5);
    private static final Duration OTP_WAIT_TTL = Duration.ofSeconds(60);
    private static final String OTP_KEY_PREFIX = "OTP_";
    private static final String OTP_WAIT_KEY_PREFIX = "OTP_WAIT_";
    private static final int OTP_LENGTH = 4;

    private final StringRedisTemplate stringRedisTemplate;

    public String generateAndStoreOtp(String email) {
        String normalizedEmail = normalizeEmail(email);
        String otp = generateOtp();
        stringRedisTemplate.opsForValue().set(key(normalizedEmail), otp, OTP_TTL);
        return otp;
    }

    public boolean isOtpValid(String email, String otp) {
        String normalizedEmail = normalizeEmail(email);
        String normalizedOtp = normalizeOtp(otp);

        String storedOtp = stringRedisTemplate.opsForValue().get(key(normalizedEmail));
        return storedOtp != null && storedOtp.equals(normalizedOtp);
    }

    public void deleteOtp(String email) {
        String normalizedEmail = normalizeEmail(email);
        stringRedisTemplate.delete(key(normalizedEmail));
    }

    public boolean hasResendLock(String email) {
        String normalizedEmail = normalizeEmail(email);
        Boolean exists = stringRedisTemplate.hasKey(waitKey(normalizedEmail));
        return Boolean.TRUE.equals(exists);
    }

    public void createResendLock(String email) {
        String normalizedEmail = normalizeEmail(email);
        stringRedisTemplate.opsForValue().set(waitKey(normalizedEmail), "1", OTP_WAIT_TTL);
    }

    private String generateOtp() {
        int number = ThreadLocalRandom.current().nextInt(0, (int) Math.pow(10, OTP_LENGTH));
        return String.format("%0" + OTP_LENGTH + "d", number);
    }

    private String key(String normalizedEmail) {
        return OTP_KEY_PREFIX + normalizedEmail;
    }

    private String waitKey(String normalizedEmail) {
        return OTP_WAIT_KEY_PREFIX + normalizedEmail;
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email boş olamaz.");
        }
        return email.trim().toLowerCase();
    }

    private String normalizeOtp(String otp) {
        if (otp == null || otp.isBlank()) {
            throw new IllegalArgumentException("OTP boş olamaz.");
        }
        return otp.trim();
    }
}

