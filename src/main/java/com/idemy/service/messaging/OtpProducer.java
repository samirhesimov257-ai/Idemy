package com.idemy.service.messaging;

import com.idemy.config.RabbitMQConfig;
import com.idemy.dto.OtpMessage;
import com.idemy.exception.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOtpMessage(OtpMessage otpMessage) {
        try {
            // Default exchange kullanıyoruz: routingKey = queue adı
            rabbitTemplate.convertAndSend(RabbitMQConfig.OTP_NOTIFICATIONS_QUEUE, otpMessage);
        } catch (Exception ex) {
            throw new MessagingException("OTP mesaj göndərilə bilmədi!", ex);
        }
    }
}

