package com.example.insuranceservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/insurance")
public class InsuranceController {

    @GetMapping("/{customerId}")
    public String verify(
            @PathVariable Long customerId
    ) throws InterruptedException {
        Thread.sleep(5000);
        return "VERIFIED";
    }
}