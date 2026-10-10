package com.tubasmart.backend.repository;
import com.tubasmart.backend.model.FloorPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FloorPriceRepository extends JpaRepository<FloorPrice, Long> {
    List<FloorPrice> findByFloorIdAndActiveTrue(Long floorId);
}
