package com.example.pharmacyservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "warehouseClient",
        url = "${warehouse.url}"
)
public interface WarehouseClient {

    @GetMapping("/api/v1/warehouse/stock/{drugId}")
    Integer getStock(
            @PathVariable Long drugId
    );
}