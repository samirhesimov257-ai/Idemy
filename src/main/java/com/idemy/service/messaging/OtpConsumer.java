package com.idemy.service.messaging;

import com.idemy.config.RabbitMQConfig;
import com.idemy.dto.OtpMessage;
import com.idemy.exception.MessagingException;
import com.idemy.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.OTP_NOTIFICATIONS_QUEUE)
    public void consumeOtpMessage(OtpMessage otpMessage) {
        try {
            emailService.sendOtpEmail(otpMessage.getEmail(), otpMessage.getOtp());
        } catch (Exception ex) {
            throw new MessagingException("OTP e-postası gönderilemedi.", ex);
        }
    }
}

