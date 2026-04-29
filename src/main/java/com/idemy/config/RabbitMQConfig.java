package com.idemy.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String ENROLLMENT_EXCHANGE = "enrollment.exchange";
    public static final String ENROLLMENT_NOTIFICATIONS_QUEUE = "enrollment.notifications";
    public static final String ENROLLMENT_ROUTING_KEY = "enrollment.notification.created";

    @Bean
    public TopicExchange enrollmentExchange() {
        return new TopicExchange(ENROLLMENT_EXCHANGE);
    }

    @Bean
    public Queue enrollmentNotificationsQueue() {
        return new Queue(ENROLLMENT_NOTIFICATIONS_QUEUE, true);
    }

    @Bean
    public Binding enrollmentNotificationBinding(Queue enrollmentNotificationsQueue, TopicExchange enrollmentExchange) {
        return BindingBuilder.bind(enrollmentNotificationsQueue)
                .to(enrollmentExchange)
                .with(ENROLLMENT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
