package com.paramins.controller;

import com.paramins.dto.*;
import com.paramins.entity.*;
import com.paramins.repository.*;
import com.paramins.service.TriggerEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final TriggerEventRepository triggerRepo;
    private final ClaimRepository        claimRepo;
    private final TriggerEngineService   triggerEngine;

    public AdminController(TriggerEventRepository triggerRepo,
                            ClaimRepository claimRepo,
                            TriggerEngineService triggerEngine) {
        this.triggerRepo   = triggerRepo;
        this.claimRepo     = claimRepo;
        this.triggerEngine = triggerEngine;
    }

    @GetMapping("/triggers/recent")
    public ResponseEntity<ApiResponse<List<TriggerEvent>>> recentTriggers(
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(ApiResponse.success("OK", triggerRepo.findRecentDays(days)));
    }

    @GetMapping("/claims/stats")
    public ResponseEntity<ApiResponse<ClaimStatsDto>> claimStats() {
        ClaimStatsDto stats = new ClaimStatsDto(
            claimRepo.count(),
            claimRepo.countByStatus(Claim.ClaimStatus.PAID),
            claimRepo.countByStatus(Claim.ClaimStatus.PENDING),
            claimRepo.countByStatus(Claim.ClaimStatus.REJECTED)
        );
        return ResponseEntity.ok(ApiResponse.success("OK", stats));
    }

    @PostMapping("/triggers/simulate")
    public ResponseEntity<ApiResponse<TriggerEvent>> simulate(
            @RequestBody TriggerSimulateRequest req) {
        TriggerEvent event = triggerEngine.manualTrigger(
            req.getCity(),
            Policy.RiskType.valueOf(req.getRiskType()),
            req.getObservedValue()
        );
        return ResponseEntity.ok(ApiResponse.success("Trigger simulated", event));
    }
}
