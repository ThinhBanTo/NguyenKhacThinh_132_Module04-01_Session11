package com.example.pharmacyservice.redis;

public record PharmacyAlert(
        String type,
        String message
) {
}