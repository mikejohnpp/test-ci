package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.social.entity.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Gửi mã OTP về email
     */
    public boolean sendOtp(String email) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) return false;

        User user = opt.get();

        // Tạo mã OTP 6 chữ số
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        long expireTime = System.currentTimeMillis() + (5 * 60 * 1000); // hết hạn sau 5 phút

        user.setOtpCode(otp);
        user.setOtpExpireTime(expireTime);
        userRepository.save(user);

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Khôi phục mật khẩu - Mã OTP");
            msg.setText("Mã OTP của bạn là: " + otp + "\nHiệu lực trong 5 phút.");
            mailSender.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xác nhận OTP hợp lệ
     */
    public boolean verifyOtp(String email, String otp) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) return false;

        User user = opt.get();
        if (user.getOtpCode() == null) return false;
        if (!user.getOtpCode().equals(otp)) return false;

        long now = System.currentTimeMillis();
        if (user.getOtpExpireTime() == null || now > user.getOtpExpireTime()) {
            return false; // OTP hết hạn
        }

        return true;
    }

    /**
     * Đặt lại mật khẩu mới
     */
    public boolean resetPassword(String email, String newPassword) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) return false;

        User user = opt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtpCode(null);
        user.setOtpExpireTime(null);
        userRepository.save(user);
        return true;
    }
}
