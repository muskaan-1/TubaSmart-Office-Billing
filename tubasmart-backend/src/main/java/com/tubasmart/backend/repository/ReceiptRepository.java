package com.tubasmart.backend.repository;
import com.tubasmart.backend.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {}
