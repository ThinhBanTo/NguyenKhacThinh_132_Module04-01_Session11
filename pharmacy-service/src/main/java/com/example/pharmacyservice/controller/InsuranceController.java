package com.example.pharmacyservice.controller;

import com.example.pharmacyservice.service.InsuranceGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/insurance")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceGatewayService insuranceGatewayService;

    @GetMapping("/{customerId}")
    public String verify(
            @PathVariable Long customerId
    ) {

        return insuranceGatewayService
                .verify(customerId)
                .join();
    }
}