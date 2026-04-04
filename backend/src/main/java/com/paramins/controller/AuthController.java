package com.paramins.controller;

import com.paramins.dto.*;
import com.paramins.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/otp/send")
    public ResponseEntity<ApiResponse<Void>> sendOtp(@RequestBody OtpRequest req) {
        authService.sendOtp(req.getPhone());
        return ResponseEntity.ok(ApiResponse.success("OTP sent", null));
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<ApiResponse<AuthResponse>> verify(@RequestBody OtpVerifyRequest req) {
        AuthResponse auth = authService.verifyOtp(req.getPhone(), req.getOtp());
        return ResponseEntity.ok(ApiResponse.success("Authenticated", auth));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody RegisterRequest req) {
        UserDto dto = UserDto.from(authService.registerWorker(req));
        return ResponseEntity.ok(ApiResponse.success("Registered", dto));
    }
}
