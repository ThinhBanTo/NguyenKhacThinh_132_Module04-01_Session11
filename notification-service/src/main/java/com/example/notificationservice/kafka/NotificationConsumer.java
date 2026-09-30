package com.example.notificationservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    @KafkaListener(
            topics = "medicine-stock-events",
            groupId = "notification-service-group"
    )
    public void consume(String message) {

        try {
            OrderEvent event =
                    objectMapper.readValue(message, OrderEvent.class);

            System.out.println("========== NOTIFICATION ==========");
            System.out.println(
                    "Hoa don cho don hang ["
                            + event.orderId()
                            + "] da duoc gui toi khach hang"
            );

            System.out.println("Medicine ID : " + event.medicineId());
            System.out.println("Quantity    : " + event.quantity());
            System.out.println("==================================");

        } catch (Exception e) {
            System.err.println(
                    "Loi NotificationConsumer: " + e.getMessage()
            );
        }
    }
}