package com.paramins.repository;
import com.paramins.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    List<Policy> findByUser(User user);
    @Query("SELECT DISTINCT p.city FROM Policy p WHERE p.status='ACTIVE'")
    List<String> findDistinctActiveCities();
    @Query("SELECT p FROM Policy p WHERE p.riskType=:rt AND p.city=:city AND p.status='ACTIVE'")
    List<Policy> findActiveByRiskTypeAndCity(@Param("rt") Policy.RiskType rt, @Param("city") String city);
}
