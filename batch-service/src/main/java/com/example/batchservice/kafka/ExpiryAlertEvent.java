package com.example.batchservice.kafka;

import java.time.LocalDate;

public record ExpiryAlertEvent(
        Long medicineId,
        String medicineName,
        Integer stock,
        LocalDate expiryDate
) {
}
