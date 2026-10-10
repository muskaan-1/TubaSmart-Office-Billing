package com.tubasmart.backend.repository;
import com.tubasmart.backend.model.SacHsnGst;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SacHsnGstRepository extends JpaRepository<SacHsnGst, Long> {
    List<SacHsnGst> findByCodeIgnoreCaseAndActiveTrue(String code);
}
