package com.example.pharmacyservice.controller;

import com.example.pharmacyservice.kafka.OrderEvent;
import com.example.pharmacyservice.kafka.OrderProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {
    private final OrderProducer  orderProducer;
    @PostMapping("/sell")
    public String sell(
            @RequestParam Long medicineId,
            @RequestParam Integer quantity
    ){
        OrderEvent orderEvent = new OrderEvent(
                UUID.randomUUID().toString(),
                medicineId,
                quantity,
                LocalDateTime.now()
        );
        orderProducer.send(orderEvent);
        return "Thanh toán thành công! Đã gửi sự kiện tới Kafka";
    }
}