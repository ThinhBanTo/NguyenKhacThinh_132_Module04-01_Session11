package com.example.pharmacyservice.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            HttpSession session
    ) {

        session.setAttribute("username", username);

        System.out.println(
                ">>> DA LUU SESSION CHO USER: " + username
        );

        return "Đăng nhập thành công: " + username;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session) {

        String username =
                (String) session.getAttribute("username");

        if (username == null) {
            return "Bạn chưa đăng nhập";
        }

        return "Xin chào: " + username;
    }
}