package com.paramins.controller;

import com.paramins.dto.*;
import com.paramins.entity.User;
import com.paramins.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/claims")
public class ClaimController {

    private final ClaimRepository claimRepo;
    private final UserRepository  userRepo;

    public ClaimController(ClaimRepository claimRepo, UserRepository userRepo) {
        this.claimRepo = claimRepo;
        this.userRepo  = userRepo;
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ClaimDto>>> myClaims(
            @AuthenticationPrincipal String phone) {
        User user = userRepo.findByPhone(phone).orElseThrow();
        List<ClaimDto> list = claimRepo.findByUserOrderByCreatedAtDesc(user)
            .stream().map(ClaimDto::from).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("OK", list));
    }
}
