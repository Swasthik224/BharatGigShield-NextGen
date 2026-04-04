package com.paramins.service;

import com.paramins.entity.User;
import com.paramins.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class FraudCheckService {

    private final ActivityLogRepository activityRepo;

    public FraudCheckService(ActivityLogRepository activityRepo) {
        this.activityRepo = activityRepo;
    }

    public FraudResult evaluate(User user, LocalDate eventDate) {
        boolean gpsMatch = activityRepo.findByUserAndLogDate(user, eventDate)
            .map(log -> user.getCity().equalsIgnoreCase(log.getGpsCity()))
            .orElse(true);  // benefit of the doubt if no GPS log

        boolean platformActive = activityRepo
            .existsByUserAndLogDateAndPlatformLogin(user, eventDate, true);

        double score = 0.0;
        if (!gpsMatch)       score += 0.4;
        if (!platformActive) score += 0.35;

        long recentClaims = activityRepo.countRecentClaims(user.getId(), eventDate.minusDays(30));
        if (recentClaims > 10) score += 0.25;

        return new FraudResult(gpsMatch, platformActive, Math.min(score, 1.0));
    }

    public static class FraudResult {
        private final boolean isGpsVerified;
        private final boolean isPlatformActive;
        private final double score;

        public FraudResult(boolean isGpsVerified, boolean isPlatformActive, double score) {
            this.isGpsVerified = isGpsVerified;
            this.isPlatformActive = isPlatformActive;
            this.score = score;
        }

        public boolean isGpsVerified()      { return isGpsVerified; }
        public boolean isPlatformActive()   { return isPlatformActive; }
        public double score()               { return score; }
    }
}
