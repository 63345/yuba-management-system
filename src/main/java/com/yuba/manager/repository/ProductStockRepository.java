package com.yuba.manager.repository;

import com.yuba.manager.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    Optional<ProductStock> findByProductName(String productName);
}
