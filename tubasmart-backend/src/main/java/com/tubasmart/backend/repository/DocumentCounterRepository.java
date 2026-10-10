package com.tubasmart.backend.repository;
import com.tubasmart.backend.model.DocumentCounter;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface DocumentCounterRepository extends JpaRepository<DocumentCounter, Long> {
    java.util.Optional<DocumentCounter> findByDocumentTypeAndFinancialYear(String documentType, String financialYear);

    /** Row-locked read: concurrent number generation waits instead of issuing duplicate numbers. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from DocumentCounter c where c.documentType=:t and c.financialYear=:fy")
    java.util.Optional<DocumentCounter> lockByTypeAndYear(@Param("t") String documentType, @Param("fy") String financialYear);
}
