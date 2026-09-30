package com.example.pharmacyservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@RefreshScope
@Service
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${app.kafka.stock-topic}")
    private String topic;

    public void send(OrderEvent event){
        kafkaTemplate.send(
                topic,
                String.valueOf(event.medicineId()),
                event
        );
        System.out.println("Da gui OrderEvent: "
        + event.orderId()
        +" | medicineId = "
        + event.medicineId()
        );
    }
}
