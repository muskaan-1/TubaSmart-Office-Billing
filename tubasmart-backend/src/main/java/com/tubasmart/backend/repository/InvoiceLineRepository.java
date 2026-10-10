package com.tubasmart.backend.repository;
import com.tubasmart.backend.model.InvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> { List<InvoiceLine> findByInvoiceId(Long invoiceId); }
