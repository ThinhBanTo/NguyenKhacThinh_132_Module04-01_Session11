package com.example.pharmacyservice.service;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @RateLimiter(
            name = "invoiceLimit",
            fallbackMethod = "invoiceFallback"
    )
    @Retry(name = "invoiceRetry")
    public String createInvoice() {

        System.out.println(
                ">>> CREATE INVOICE: "
                        + java.time.LocalTime.now()
        );

        return "Xuất hóa đơn thành công";
    }

    public String invoiceFallback(
            Throwable ex
    ) {

        return "Không thể xuất hóa đơn lúc này";
    }
}