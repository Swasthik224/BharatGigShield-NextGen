package com.paramins.service;

import com.paramins.entity.Claim;
import com.paramins.entity.Payment;
import com.paramins.repository.ClaimRepository;
import com.paramins.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PayoutService {

    private static final Logger log = LoggerFactory.getLogger(PayoutService.class);

    private final PaymentRepository paymentRepo;
    private final ClaimRepository   claimRepo;
    private final NotificationService notify;

    @Value("${app.payout.upi-primary:true}")
    private boolean upiPrimary;

    public PayoutService(PaymentRepository paymentRepo,
                         ClaimRepository claimRepo,
                         NotificationService notify) {
        this.paymentRepo = paymentRepo;
        this.claimRepo   = claimRepo;
        this.notify      = notify;
    }

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    @Transactional
    public void disbursePayout(Claim claim) {
        String ref = "PAY-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();

        Payment payment = Payment.builder()
            .referenceId(ref)
            .type(Payment.PaymentType.PAYOUT)
            .direction(Payment.Direction.OUTBOUND)
            .entityType(Payment.EntityType.CLAIM)
            .entityId(claim.getId())
            .user(claim.getUser())
            .amount(claim.getPayoutAmount())
            .channel(Payment.Channel.UPI)
            .upiId(claim.getUser().getUpiId())
            .status(Payment.PaymentStatus.PROCESSING)
            .build();
        payment = paymentRepo.save(payment);

        try {
            String txnId = mockUpiPayout(claim.getUser().getUpiId(), claim.getPayoutAmount(), ref);
            payment.setChannelRef(txnId);
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setCompletedAt(LocalDateTime.now());
            paymentRepo.save(payment);

            claim.setStatus(Claim.ClaimStatus.PAID);
            claim.setSettledAt(LocalDateTime.now());
            claimRepo.save(claim);

            notify.sendPayoutSuccess(claim.getUser(), claim);
            log.info("PAYOUT SUCCESS: {} => ₹{} txn={}",
                claim.getClaimNumber(), claim.getPayoutAmount() / 100, txnId);

        } catch (Exception e) {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason(e.getMessage());
            payment.setAttemptCount(payment.getAttemptCount() + 1);
            paymentRepo.save(payment);
            log.error("Payout failed for {}: {}", claim.getClaimNumber(), e.getMessage());
            throw e;
        }
    }

    /** Mock UPI — replace with NPCI / Razorpay Payouts API in production */
    private String mockUpiPayout(String upiId, int amountPaise, String ref) {
        if (Math.random() < 0.05) {
            throw new RuntimeException("UPI_TIMEOUT");
        }
        log.info("[UPI MOCK] Sending ₹{} to {}", amountPaise / 100, upiId);
        return "UPI" + System.currentTimeMillis();
    }
}
