package com.paramins.repository;
import com.paramins.entity.*;
import org.springframework.data.jpa.repository.*;
import java.util.List;
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    boolean existsByPolicyAndTriggerEvent(Policy policy, TriggerEvent trigger);
    List<Claim> findByUserOrderByCreatedAtDesc(User user);
    long countByStatus(Claim.ClaimStatus status);
}
