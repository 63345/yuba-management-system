package com.yuba.manager.repository;

import com.yuba.manager.entity.ProductionBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductionBatchRepository extends JpaRepository<ProductionBatch, Long> {
    List<ProductionBatch> findByStatus(String status);
}
