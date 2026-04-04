package com.paramins.controller;

import com.paramins.dto.ApiResponse;
import com.paramins.entity.ActivityLog;
import com.paramins.entity.User;
import com.paramins.repository.ActivityLogRepository;
import com.paramins.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/activity")
public class ActivityLogController {

    private final ActivityLogRepository activityRepo;
    private final UserRepository        userRepo;

    public ActivityLogController(ActivityLogRepository activityRepo, UserRepository userRepo) {
        this.activityRepo = activityRepo;
        this.userRepo     = userRepo;
    }

    @PostMapping("/log")
    public ResponseEntity<ApiResponse<String>> logActivity(
            @RequestBody ActivityRequest req,
            @AuthenticationPrincipal String phone) {
        User user = userRepo.findByPhone(phone).orElseThrow();
        ActivityLog log = ActivityLog.builder()
            .user(user)
            .logDate(req.getLogDate() != null ? req.getLogDate() : LocalDate.now())
            .activeHours(BigDecimal.valueOf(req.getActiveHours()))
            .deliveries(req.getDeliveries())
            .platformLogin(true)
            .gpsCity(req.getGpsCity() != null ? req.getGpsCity() : user.getCity())
            .build();
        activityRepo.save(log);
        return ResponseEntity.ok(ApiResponse.success("Activity logged", "OK"));
    }

    /** Seeds 15 days of activity so underwriting check passes (dev/test only). */
    @PostMapping("/seed-test-data")
    public ResponseEntity<ApiResponse<String>> seedTestData(
            @AuthenticationPrincipal String phone) {
        User user = userRepo.findByPhone(phone).orElseThrow();
        for (int i = 1; i <= 15; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            if (!activityRepo.existsByUserAndLogDateAndPlatformLogin(user, date, true)) {
                ActivityLog log = ActivityLog.builder()
                    .user(user).logDate(date)
                    .activeHours(BigDecimal.valueOf(8))
                    .deliveries(12).platformLogin(true)
                    .gpsCity(user.getCity()).build();
                activityRepo.save(log);
            }
        }
        return ResponseEntity.ok(ApiResponse.success("Seeded 15 days of activity", "OK"));
    }

    // ── Inner request DTO ─────────────────────────────────────
    public static class ActivityRequest {
        private LocalDate logDate;
        private double activeHours;
        private int deliveries;
        private String gpsCity;
        private Double lat, lng;

        public LocalDate getLogDate()           { return logDate; }
        public void setLogDate(LocalDate v)     { this.logDate = v; }
        public double getActiveHours()          { return activeHours; }
        public void setActiveHours(double v)    { this.activeHours = v; }
        public int getDeliveries()              { return deliveries; }
        public void setDeliveries(int v)        { this.deliveries = v; }
        public String getGpsCity()              { return gpsCity; }
        public void setGpsCity(String v)        { this.gpsCity = v; }
        public Double getLat()                  { return lat; }
        public void setLat(Double v)            { this.lat = v; }
        public Double getLng()                  { return lng; }
        public void setLng(Double v)            { this.lng = v; }
    }
}
