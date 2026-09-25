package com.example.pharmacyservice.controller;

import com.example.pharmacyservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public String createInvoice() {

        return invoiceService.createInvoice();
    }
}