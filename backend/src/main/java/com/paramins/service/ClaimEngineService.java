package com.paramins.service;

import com.paramins.entity.*;
import com.paramins.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ClaimEngineService {

    private static final Logger log = LoggerFactory.getLogger(ClaimEngineService.class);

    private final PolicyRepository      policyRepo;
    private final ClaimRepository       claimRepo;
    private final ActivityLogRepository activityRepo;
    private final FraudCheckService     fraudCheck;
    private final PayoutService         payoutService;
    private final NotificationService   notifyService;

    public ClaimEngineService(PolicyRepository policyRepo,
                               ClaimRepository claimRepo,
                               ActivityLogRepository activityRepo,
                               FraudCheckService fraudCheck,
                               PayoutService payoutService,
                               NotificationService notifyService) {
        this.policyRepo   = policyRepo;   this.claimRepo    = claimRepo;
        this.activityRepo = activityRepo; this.fraudCheck   = fraudCheck;
        this.payoutService = payoutService; this.notifyService = notifyService;
    }

    /** Zero-touch: called automatically when a TriggerEvent fires. */
    @Transactional
    public void processTriggeredClaims(TriggerEvent trigger) {
        List<Policy> eligible = policyRepo
            .findActiveByRiskTypeAndCity(trigger.getRiskType(), trigger.getCity());

        log.info("Processing {} policies for trigger {} in {}",
            eligible.size(), trigger.getRiskType(), trigger.getCity());

        for (Policy policy : eligible) {
            try {
                processSingleClaim(policy, trigger);
            } catch (Exception ex) {
                log.error("Claim failed for policy {}: {}", policy.getId(), ex.getMessage());
            }
        }
    }

    private void processSingleClaim(Policy policy, TriggerEvent trigger) {
        // 1. Duplicate guard
        if (claimRepo.existsByPolicyAndTriggerEvent(policy, trigger)) return;

        // 2. Worker must have been active on trigger day
        boolean wasActive = activityRepo.existsByUserAndLogDateAndPlatformLogin(
            policy.getUser(), trigger.getEventDate(), true);
        if (!wasActive) {
            log.info("Worker {} not active on {}", policy.getUser().getId(), trigger.getEventDate());
            return;
        }

        // 3. Create claim
        String claimNum = "CLM-" + LocalDate.now().getYear() + "-"
            + String.format("%06d", claimRepo.count() + 1);

        Claim claim = Claim.builder()
            .claimNumber(claimNum)
            .policy(policy)
            .user(policy.getUser())
            .triggerEvent(trigger)
            .claimDate(trigger.getEventDate())
            .triggerDays(1)
            .payoutAmount(policy.getPayoutPerDay())
            .status(Claim.ClaimStatus.FRAUD_CHECK)
            .build();
        claim = claimRepo.save(claim);

        // 4. Fraud check
        FraudCheckService.FraudResult fraud =
            fraudCheck.evaluate(policy.getUser(), trigger.getEventDate());
        claim.setGpsVerified(fraud.isGpsVerified());
        claim.setPlatformActive(fraud.isPlatformActive());
        claim.setFraudScore(BigDecimal.valueOf(fraud.score()));

        if (fraud.score() > 0.7) {
            claim.setStatus(Claim.ClaimStatus.REJECTED);
            claim.setFraudNote("Fraud score " + fraud.score() + " exceeds threshold 0.70");
            claimRepo.save(claim);
            log.warn("Claim REJECTED (fraud score {}): {}", fraud.score(), claimNum);
            return;
        }

        // 5. Approve and pay
        claim.setStatus(Claim.ClaimStatus.APPROVED);
        claimRepo.save(claim);
        payoutService.disbursePayout(claim);
    }
}
