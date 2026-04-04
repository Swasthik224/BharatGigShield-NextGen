package com.paramins.repository;
import com.paramins.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT COALESCE(SUM(p.amount),0) FROM Payment p WHERE p.type=:type AND p.status='SUCCESS' AND p.user.city=:city")
    long sumByTypeAndCity(@Param("type") Payment.PaymentType type, @Param("city") String city);
}
