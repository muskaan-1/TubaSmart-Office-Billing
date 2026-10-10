package com.tubasmart.backend.repository;
import com.tubasmart.backend.model.TenantChargeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TenantChargeScheduleRepository extends JpaRepository<TenantChargeSchedule, Long> {
    List<TenantChargeSchedule> findByAllotmentIdAndActiveTrue(Long allotmentId);
}
