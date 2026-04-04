package com.paramins.service;

import com.paramins.entity.Claim;
import com.paramins.entity.TriggerEvent;
import com.paramins.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendPayoutSuccess(User user, Claim claim) {
        // TODO: integrate Firebase FCM + MSG91 SMS
        log.info("[NOTIFY] Payout ₹{} sent to {} ({})",
            claim.getPayoutAmount() / 100, user.getName(), user.getPhone());
    }

    public void sendTriggerAlert(User user, TriggerEvent event) {
        log.info("[NOTIFY] Trigger alert for {} in {}: {} {}",
            user.getName(), event.getCity(), event.getRiskType(), event.getObservedValue());
    }
}
