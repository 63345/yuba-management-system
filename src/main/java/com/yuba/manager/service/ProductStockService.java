package com.yuba.manager.service;

import com.yuba.manager.entity.ProductStock;
import com.yuba.manager.repository.ProductStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductStockService {

    private final ProductStockRepository productStockRepository;

    public ProductStockService(ProductStockRepository productStockRepository) {
        this.productStockRepository = productStockRepository;
    }

    public List<ProductStock> findAll() {
        return productStockRepository.findAll();
    }

    @Transactional
    public void addStock(String productName, Double quantity, String unit) {
        ProductStock stock = productStockRepository.findByProductName(productName)
                .orElse(new ProductStock());
        
        if (stock.getId() == null) {
            stock.setProductName(productName);
            stock.setQuantity(0.0);
            stock.setUnit(unit);
            stock.setWarehouseLocation("Main Warehouse");
        }
        
        stock.setQuantity(stock.getQuantity() + quantity);
        productStockRepository.save(stock);
    }
}
