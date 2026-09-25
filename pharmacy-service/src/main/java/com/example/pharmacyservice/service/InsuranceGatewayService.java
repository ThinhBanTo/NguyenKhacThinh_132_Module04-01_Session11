package com.example.pharmacyservice.service;

import com.example.pharmacyservice.client.InsuranceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class InsuranceGatewayService {

    private final InsuranceClient insuranceClient;

    @TimeLimiter(
            name = "insuranceTimeout",
            fallbackMethod = "insuranceFallback"
    )
    @Retry(name = "insuranceRetry")
    @CircuitBreaker(name = "insuranceCB")
    public CompletableFuture<String> verify(
            Long customerId
    ) {

        return CompletableFuture.supplyAsync(
                () -> insuranceClient.verify(customerId)
        );
    }

    public CompletableFuture<String> insuranceFallback(
            Long customerId,
            Throwable ex
    ) {

        System.out.println(
                ">>> INSURANCE FALLBACK"
        );

        return CompletableFuture.completedFuture(
                "Xác thực bảo hiểm sau"
        );
    }
}