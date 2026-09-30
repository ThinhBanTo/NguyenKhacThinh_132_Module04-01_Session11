package com.example.warehouseservice.kafka;

import java.time.LocalDateTime;

public record OrderEvent(
        String orderId,
        Long medicineId,
        Integer quantity,
        LocalDateTime timestamp
) {
}
