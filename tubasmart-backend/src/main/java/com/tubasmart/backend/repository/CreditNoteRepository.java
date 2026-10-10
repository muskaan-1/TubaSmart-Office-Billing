package com.tubasmart.backend.repository;
import com.tubasmart.backend.model.CreditNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CreditNoteRepository extends JpaRepository<CreditNote, Long> {
    List<CreditNote> findByOriginalInvoiceId(Long invoiceId);
}
