package com.example.pharmacyservice.service;

import com.example.pharmacyservice.client.WarehouseClient;
import com.example.pharmacyservice.entity.Drug;
import com.example.pharmacyservice.repo.DrugRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseGatewayService {

    private final WarehouseClient warehouseClient;
    private final DrugRepository drugRepository;

    @CircuitBreaker(
            name = "warehouseCB",
            fallbackMethod = "checkWarehouseFallback"
    )
    public Integer checkStock(Long drugId) {

        return warehouseClient.getStock(drugId);
    }

    public Integer checkWarehouseFallback(
            Long drugId,
            Throwable ex
    ) {

        System.out.println(
                ">>> KHÔNG THỂ KẾT NỐI KHO TỔNG"
        );

        Drug drug = drugRepository
                .findById(drugId)
                .orElseThrow();

        return drug.getStock();
    }
}