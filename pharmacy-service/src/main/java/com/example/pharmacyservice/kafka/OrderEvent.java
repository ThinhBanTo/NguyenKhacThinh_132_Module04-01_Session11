package com.example.pharmacyservice.kafka;

import java.time.LocalDateTime;

public record OrderEvent(
        String orderId,
        Long medicineId,
        Integer quantity,
        LocalDateTime timestamp
) {
}