package com.paramins.service;

import com.paramins.dto.AuthResponse;
import com.paramins.dto.RegisterRequest;
import com.paramins.entity.OtpSession;
import com.paramins.entity.User;
import com.paramins.repository.OtpSessionRepository;
import com.paramins.repository.UserRepository;
import com.paramins.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final OtpSessionRepository otpRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepo, OtpSessionRepository otpRepo,
                       JwtUtil jwtUtil, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.otpRepo = otpRepo;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    // 🔹 SEND OTP
    public void sendOtp(String phone) {
        String otp = String.format("%06d", new SecureRandom().nextInt(1_000_000));

        OtpSession session = OtpSession.builder()
                .phone(phone)
                .otpHash(encoder.encode(otp))
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        otpRepo.save(session);

        System.out.println("==========================================");
        System.out.println("[MOCK OTP] Phone: " + phone + "  OTP: " + otp);
        System.out.println("==========================================");
    }

    // 🔹 VERIFY OTP + RETURN ROLE (UPDATED WITH ADMIN LOGIC)
    @Transactional
    public AuthResponse verifyOtp(String phone, String otp) {

        OtpSession session = otpRepo
                .findLatestValid(phone, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("OTP expired or not found."));

        if (!encoder.matches(otp, session.getOtpHash())) {
            throw new RuntimeException("Invalid OTP");
        }

        session.setUsed(true);
        otpRepo.save(session);

        boolean isNew = !userRepo.existsByPhone(phone);

        User user = userRepo.findByPhone(phone).orElse(null);

        String token = jwtUtil.generateToken(phone);

        // 🔥 ROLE LOGIC (ADMIN + USER)
        String role;

        if (phone.equals("9876543210")) {
            role = "ADMIN"; // 👈 your admin number
        } 
        else if (user == null) {
            role = "USER";
        } 
        else {
            role = user.getRole().name();
        }

        return new AuthResponse(token, isNew, phone, role);
    }

    // 🔹 REGISTER USER WITH DEFAULT ROLE
    @Transactional
    public User registerWorker(RegisterRequest req) {

        if (userRepo.existsByPhone(req.getPhone())) {
            return userRepo.findByPhone(req.getPhone()).orElseThrow();
        }

        User user = User.builder()
                .phone(req.getPhone())
                .name(req.getName())
                .city(req.getCity())
                .upiId(req.getUpiId())
                .platform(User.Platform.valueOf(req.getPlatform()))
                .tier(User.WorkerTier.TIER_C)
                .role(User.Role.USER) // ✅ default USER
                .build();

        return userRepo.save(user);
    }
}