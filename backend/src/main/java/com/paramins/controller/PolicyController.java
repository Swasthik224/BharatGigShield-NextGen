package com.paramins.controller;

import com.paramins.dto.*;
import com.paramins.entity.*;
import com.paramins.repository.*;
import com.paramins.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController {

    private final PricingEngineService pricing;
    private final PolicyRepository     policyRepo;
    private final UnderwritingService  underwriting;
    private final UserRepository       userRepo;

    public PolicyController(PricingEngineService pricing, PolicyRepository policyRepo,
                             UnderwritingService underwriting, UserRepository userRepo) {
        this.pricing = pricing; this.policyRepo = policyRepo;
        this.underwriting = underwriting; this.userRepo = userRepo;
    }

    @GetMapping("/quote")
    public ResponseEntity<ApiResponse<PremiumQuoteResponse>> quote(
            @RequestParam String city,
            @RequestParam String riskType,
            @AuthenticationPrincipal String phone) {
        User user = userRepo.findByPhone(phone).orElseThrow();
        PremiumQuoteResponse q = pricing.quote(city, Policy.RiskType.valueOf(riskType), user.getTier());
        return ResponseEntity.ok(ApiResponse.success("Quote generated", q));
    }

    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse<PolicyDto>> enroll(
            @RequestBody EnrollRequest req,
            @AuthenticationPrincipal String phone) {
        User user = userRepo.findByPhone(phone).orElseThrow();
        underwriting.checkEligibility(user);
        user.setTier(underwriting.calculateTier(user));

        Policy.RiskType rt = Policy.RiskType.valueOf(req.getRiskType());
        double threshold;
        switch (rt) {
            case AQI:  threshold = 300; break;
            case RAIN: threshold = 50;  break;
            default:   threshold = 42;  break;
        }

        PremiumQuoteResponse q = pricing.quote(req.getCity(), rt, user.getTier());

        String pnum = "PI-" + LocalDate.now().getYear()
            + "-" + String.format("%06d", policyRepo.count() + 1);

        Policy policy = Policy.builder()
            .policyNumber(pnum)
            .user(user)
            .riskType(rt)
            .city(req.getCity())
            .weeklyPremium(q.getWeeklyPremiumPaise())
            .payoutPerDay(q.getPayoutPerDayPaise())
            .triggerThreshold(BigDecimal.valueOf(threshold))
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusYears(1))
            .nextBillingDate(LocalDate.now().plusWeeks(1))
            .build();

        Policy saved = policyRepo.save(policy);
        return ResponseEntity.ok(ApiResponse.success("Policy created", PolicyDto.from(saved)));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<PolicyDto>>> myPolicies(
            @AuthenticationPrincipal String phone) {
        User user = userRepo.findByPhone(phone).orElseThrow();
        List<PolicyDto> list = policyRepo.findByUser(user).stream()
            .map(PolicyDto::from).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("OK", list));
    }
}
