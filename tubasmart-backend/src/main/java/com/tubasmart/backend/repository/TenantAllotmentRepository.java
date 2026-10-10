package com.tubasmart.backend.repository;
import com.tubasmart.backend.model.TenantAllotment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TenantAllotmentRepository extends JpaRepository<TenantAllotment, Long> {
    boolean existsByFloorIdAndActiveTrue(Long floorId);
    List<TenantAllotment> findByTenantIdAndActiveTrue(Long tenantId);
    List<TenantAllotment> findByActiveTrue();
}
