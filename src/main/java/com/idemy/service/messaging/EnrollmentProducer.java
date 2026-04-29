package com.idemy.service.messaging;

import com.idemy.config.RabbitMQConfig;
import com.idemy.dto.NotificationDTO;
import com.idemy.exception.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEnrollmentNotification(NotificationDTO notificationDTO) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ENROLLMENT_EXCHANGE,
                    RabbitMQConfig.ENROLLMENT_ROUTING_KEY,
                    notificationDTO
            );
        } catch (AmqpException ex) {
            throw new MessagingException("Enrollment notification could not be sent.", ex);
        }
    }
}
