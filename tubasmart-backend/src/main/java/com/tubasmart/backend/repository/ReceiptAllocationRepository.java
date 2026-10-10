package com.tubasmart.backend.repository;
import com.tubasmart.backend.model.ReceiptAllocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.*;
public interface ReceiptAllocationRepository extends JpaRepository<ReceiptAllocation, Long> {
    @Query("select coalesce(sum(r.amount),0) from ReceiptAllocation r where r.invoice.id=:invoiceId") BigDecimal sumAmountByInvoiceId(@Param("invoiceId") Long invoiceId);
    List<ReceiptAllocation> findByInvoiceId(Long invoiceId);
    List<ReceiptAllocation> findByReceiptId(Long receiptId);
}
