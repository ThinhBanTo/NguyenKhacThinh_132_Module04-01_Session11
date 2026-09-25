package com.example.pharmacyservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RefreshScope
@RestController
@RequestMapping("/api/v1/bill")
public class BillController {

    @Value("${pharmacy.vat-rate}")
    private double vatRate;

    @PostMapping
    public Map<String, Object> createBill(
            @RequestParam double price
    ) {

        double total = price + price * vatRate;

        return Map.of(
                "price", price,
                "vatRate", vatRate,
                "total", total
        );
    }
}