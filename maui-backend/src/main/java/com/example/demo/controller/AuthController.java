package com.example.demo.controller;

import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    @PostMapping("/forgot-password")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        try {
            boolean ok = authService.sendOtp(email);
            if (ok) {
                return ResponseEntity.ok(Map.of(
                        "message", "OTP đã được gửi thành công",
                        "success", true
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "Không thể gửi OTP hoặc email không tồn tại",
                        "success", false
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Lỗi server: " + e.getMessage(),
                    "success", false
            ));
        }
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean ok = authService.verifyOtp(email, otp);
        return ok ? ResponseEntity.ok(Map.of(
                "message", "OTP hợp lệ",
                "success", true
        )) :
                ResponseEntity.badRequest().body(Map.of(
                        "message", "OTP không hợp lệ hoặc đã hết hạn",
                        "success", false
                ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        boolean ok = authService.resetPassword(email, newPassword);
        return ok ? ResponseEntity.ok(Map.of(
                "message", "Mật khẩu đã được đặt lại",
                "success", true
        )) :
                ResponseEntity.badRequest().body(Map.of(
                        "message", "Không thể đặt lại mật khẩu",
                        "success", false
                ));
    }
}
