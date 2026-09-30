package com.example.warehouseservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    @KafkaListener(
            topics = "medicine-stock-events",
            groupId = "inventory-service-group"
    )
    public void consume(String message) {
        try {

            OrderEvent event =
                    objectMapper.readValue(message, OrderEvent.class);

            System.out.println("========== KAFKA CONSUMER ==========");
            System.out.println("Order ID    : " + event.orderId());
            System.out.println("Medicine ID : " + event.medicineId());
            System.out.println("Quantity    : " + event.quantity());
            System.out.println("Timestamp   : " + event.timestamp());
            System.out.println("====================================");

        } catch (Exception e) {
            System.err.println("Loi khi doc OrderEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }
}