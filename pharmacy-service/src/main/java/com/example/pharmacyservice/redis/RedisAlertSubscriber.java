package com.example.pharmacyservice.redis;

import org.springframework.stereotype.Service;

@Service
public class RedisAlertSubscriber {

    public void receiveMessage(String message) {

        System.out.println();
        System.out.println(
                "===== THONG BAO DASHBOARD QUAN LY ====="
        );

        System.out.println(
                "Nhan message: " + message
        );

        System.out.println(
                "======================================"
        );
        System.out.println();
    }
}