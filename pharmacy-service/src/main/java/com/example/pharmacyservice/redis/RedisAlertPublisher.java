package com.example.pharmacyservice.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class RedisAlertPublisher {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void publish(PharmacyAlert alert) {

        try {
            String json = objectMapper.writeValueAsString(alert);

            redisTemplate.convertAndSend(
                    "pharmacy-alerts",
                    json
            );

            System.out.println(
                    ">>> DA GUI THONG BAO LEN CHANNEL pharmacy-alerts"
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Loi gui Redis Pub/Sub",
                    e
            );
        }
    }
}