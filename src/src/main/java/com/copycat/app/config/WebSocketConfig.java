package com.copycat.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Точка входу: сюди фронтенд буде стукати, щоб відкрити з'єднання
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*"); // Дозволяємо підключення з будь-яких доменів/портів
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /topic - префікс для каналів, на які клієнти будуть підписуватися (наприклад, /topic/room/1234)
        registry.enableSimpleBroker("/topic");
        
        // /app - префікс для повідомлень, які клієнти ВІДПРАВЛЯЮТЬ на сервер
        registry.setApplicationDestinationPrefixes("/app");
    }
}