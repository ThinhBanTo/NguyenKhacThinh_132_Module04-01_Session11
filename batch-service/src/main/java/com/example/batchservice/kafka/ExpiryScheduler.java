package com.example.batchservice.kafka;

import com.example.batchservice.entity.Drug;
import com.example.batchservice.repo.DrugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpiryScheduler {

    private final DrugRepository drugRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.expiry.days}")
    private int expiryDays;

    @Value("${app.kafka.notification-topic}")
    private String topic;

    @Scheduled(cron = "${app.expiry.cron}")
    public void checkExpiringDrugs() {

        LocalDate today = LocalDate.now();
        LocalDate deadline = today.plusDays(expiryDays);

        List<Drug> drugs =
                drugRepository.findByExpiryDateBetween(
                        today,
                        deadline
                );

        if (drugs.isEmpty()) {
            System.out.println(
                    "Khong co thuoc sap het han."
            );
            return;
        }

        List<ExpiryAlertEvent> events =
                drugs.stream()
                        .map(drug ->
                                new ExpiryAlertEvent(
                                        drug.getId(),
                                        drug.getName(),
                                        drug.getStock(),
                                        drug.getExpiryDate()
                                )
                        )
                        .toList();

        kafkaTemplate.send(
                topic,
                "expiry-alert",
                events
        ).whenComplete((result, ex) -> {

            if (ex == null) {

                System.out.println(
                        "===== GUI CANH BAO THANH CONG ====="
                );

                System.out.println(
                        "So thuoc sap het han: "
                                + events.size()
                );

                System.out.println(
                        "Topic: "
                                + result.getRecordMetadata().topic()
                );

            } else {

                System.err.println(
                        "GUI CANH BAO THAT BAI: "
                                + ex.getMessage()
                );
            }
        });
    }
}