package com.paramins.repository;
import com.paramins.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
public interface TriggerEventRepository extends JpaRepository<TriggerEvent, Long> {
    boolean existsByCityAndRiskTypeAndEventDate(String city, Policy.RiskType riskType, LocalDate date);
    @Query("SELECT t FROM TriggerEvent t WHERE t.eventDate>=:from ORDER BY t.eventDate DESC")
    List<TriggerEvent> findRecentEvents(@Param("from") LocalDate from);
    default List<TriggerEvent> findRecentDays(int days) {
        return findRecentEvents(LocalDate.now().minusDays(days));
    }
}
