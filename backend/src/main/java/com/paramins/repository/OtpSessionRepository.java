package com.paramins.repository;
import com.paramins.entity.OtpSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Optional;
public interface OtpSessionRepository extends JpaRepository<OtpSession, Long> {
    @Query("SELECT o FROM OtpSession o WHERE o.phone=:phone AND o.used=false AND o.expiresAt>:now ORDER BY o.createdAt DESC")
    Optional<OtpSession> findLatestValid(@Param("phone") String phone, @Param("now") LocalDateTime now);
}
