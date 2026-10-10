package com.tubasmart.backend.repository;
import com.tubasmart.backend.model.ChargeType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ChargeTypeRepository extends JpaRepository<ChargeType, Long> {
    List<ChargeType> findByNameIgnoreCaseOrCodeIgnoreCase(String name, String code);
}
