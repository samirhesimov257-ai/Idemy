package com.idemy.service.messaging;

import com.idemy.config.RabbitMQConfig;
import com.idemy.dto.NotificationDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @RabbitListener(queues = RabbitMQConfig.ENROLLMENT_NOTIFICATIONS_QUEUE)
    public void consumeEnrollmentNotification(NotificationDTO notificationDTO) {
        System.out.println("Sending enrollment email to " + notificationDTO.getEmail()
                + " for course " + notificationDTO.getCourseName());
    }
}
