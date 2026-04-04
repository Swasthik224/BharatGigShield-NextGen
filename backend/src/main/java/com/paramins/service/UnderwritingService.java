package com.paramins.service;

import com.paramins.entity.User;
import com.paramins.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class UnderwritingService {

    private final ActivityLogRepository activityRepo;

    public UnderwritingService(ActivityLogRepository activityRepo) {
        this.activityRepo = activityRepo;
    }

    public void checkEligibility(User user) {
        long activeDays = activityRepo.countActiveDaysInRange(
            user.getId(), LocalDate.now().minusDays(30), LocalDate.now());
        if (activeDays < 7) {
            throw new RuntimeException(
                "Minimum 7 active delivery days required. You have: " + activeDays);
        }
    }

    public User.WorkerTier calculateTier(User user) {
        long days = activityRepo.countActiveDaysInRange(
            user.getId(), LocalDate.now().minusDays(30), LocalDate.now());
        if (days >= 15) return User.WorkerTier.TIER_A;
        if (days >= 7)  return User.WorkerTier.TIER_B;
        return User.WorkerTier.TIER_C;
    }
}
