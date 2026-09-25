package com.example.pharmacyservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "insuranceClient",
        url = "${insurance.url}"
)
public interface InsuranceClient {

    @GetMapping("/api/v1/insurance/{customerId}")
    String verify(
            @PathVariable Long customerId
    );
}