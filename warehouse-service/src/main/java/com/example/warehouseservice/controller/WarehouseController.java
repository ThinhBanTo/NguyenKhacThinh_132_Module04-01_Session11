package com.example.warehouseservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    @GetMapping("/stock/{drugId}")
    public Integer getStock(
            @PathVariable Long drugId
    ) {
        return 100;
    }
}
