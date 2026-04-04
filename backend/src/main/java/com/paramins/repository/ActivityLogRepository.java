package com.paramins.repository;
import com.paramins.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.Optional;
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    Optional<ActivityLog> findByUserAndLogDate(User user, LocalDate date);
    boolean existsByUserAndLogDateAndPlatformLogin(User user, LocalDate date, Boolean platformLogin);
    @Query("SELECT COUNT(a) FROM ActivityLog a WHERE a.user.id=:uid AND a.logDate BETWEEN :from AND :to AND a.platformLogin=true")
    long countActiveDaysInRange(@Param("uid") Long uid, @Param("from") LocalDate from, @Param("to") LocalDate to);
    @Query("SELECT COUNT(c) FROM Claim c WHERE c.user.id=:uid AND c.claimDate>=:since")
    long countRecentClaims(@Param("uid") Long uid, @Param("since") LocalDate since);
}
