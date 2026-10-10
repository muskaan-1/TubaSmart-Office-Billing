package com.tubasmart.backend.repository;
import com.tubasmart.backend.model.*;
import org.springframework.data.jpa.repository.*;
import java.time.YearMonth;
import java.util.*;
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    boolean existsByTenantIdAndBillingPeriodAndStatusNot(Long tenantId, YearMonth billingPeriod, DocumentStatus status);
    List<Invoice> findByBillingPeriod(YearMonth billingPeriod);
}
