package com.example.pharmacyservice.controller;

import com.example.pharmacyservice.service.WarehouseGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseGatewayService warehouseGatewayService;

    @GetMapping("/stock/{drugId}")
    public Integer getStock(
            @PathVariable Long drugId
    ) {

        return warehouseGatewayService.checkStock(drugId);
    }
}