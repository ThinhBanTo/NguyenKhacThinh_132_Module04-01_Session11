package com.example.pharmacyservice.controller;

import com.example.pharmacyservice.dto.WarehouseExportRequest;
import com.example.pharmacyservice.entity.Drug;
import com.example.pharmacyservice.kafka.OrderProducer;
import com.example.pharmacyservice.redis.PharmacyAlert;
import com.example.pharmacyservice.redis.RedisAlertPublisher;
import com.example.pharmacyservice.service.DrugService;
import com.example.pharmacyservice.service.WarehouseExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {

    private final OrderProducer orderProducer;
    private final DrugService drugService;
    private final RedisAlertPublisher redisAlertPublisher;
    private final WarehouseExportService warehouseExportService;

    @GetMapping("/drugs/{id}")
    public Drug getDrugById(@PathVariable Long id) {
        return drugService.getDrugById(id);
    }
    @PutMapping("/drugs/{id}")
    public Drug updateDrug(
            @PathVariable Long id,
            @RequestBody Drug request
    ) {
        return drugService.updateDrug(id, request);
    }
    @PostMapping("/alerts")
    public String sendAlert() {

        PharmacyAlert alert = new PharmacyAlert(
                "IMPORT",
                "Đã nhập 100 hộp Panadol"
        );

        redisAlertPublisher.publish(alert);

        return "Đã gửi thông báo đến kênh pharmacy-alerts";
    }
    @PutMapping("/drugs/{id}/sell")
    public String sellMedicine(
            @PathVariable Long id
    ) {
        return drugService.sellMedicine(id);
    }
    @PutMapping("/drugs/{id}/warehouse-export")
    public String warehouseExport(
            @PathVariable Long id,
            @RequestBody WarehouseExportRequest request
    ) {
        return warehouseExportService.exportWarehouse(id, request);
    }
}