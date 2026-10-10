package com.tubasmart.backend.repository;
import com.tubasmart.backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CustomerRepository extends JpaRepository<Customer, Long> {}
