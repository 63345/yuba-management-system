package com.yuba.manager.service;

import com.yuba.manager.entity.Material;
import com.yuba.manager.entity.ProductionBatch;
import com.yuba.manager.repository.MaterialRepository;
import com.yuba.manager.repository.ProductionBatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductionService {

    private final ProductionBatchRepository productionBatchRepository;
    private final MaterialRepository materialRepository;
    private final ProductStockService productStockService;

    public ProductionService(ProductionBatchRepository productionBatchRepository, 
                             MaterialRepository materialRepository,
                             ProductStockService productStockService) {
        this.productionBatchRepository = productionBatchRepository;
        this.materialRepository = materialRepository;
        this.productStockService = productStockService;
    }

    public List<ProductionBatch> findAll() {
        return productionBatchRepository.findAll();
    }

    @Transactional
    public void startBatch(String batchCode, Long materialId, Double materialQuantity) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        if (material.getCurrentStock() < materialQuantity) {
            throw new RuntimeException("Insufficient material stock (原料库存不足)");
        }

        // Deduct material
        material.setCurrentStock(material.getCurrentStock() - materialQuantity);
        materialRepository.save(material);

        // Create Batch
        ProductionBatch batch = new ProductionBatch();
        batch.setBatchCode(batchCode);
        batch.setMaterialUsed(material);
        batch.setMaterialQuantityUsed(materialQuantity);
        batch.setStartTime(LocalDateTime.now());
        batch.setStatus("PROCESSING"); // In Chinese: 生产中
        
        productionBatchRepository.save(batch);
    }

    @Transactional
    public void completeBatch(Long batchId, Double outputQuantity, String qualityGrade) {
        ProductionBatch batch = productionBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        if ("COMPLETED".equals(batch.getStatus()) || "已完成".equals(batch.getStatus())) {
             throw new RuntimeException("Batch is already completed");
        }

        batch.setStatus("COMPLETED"); // In Chinese: 已完成 (Adjust based on your detailed design)
        batch.setEndTime(LocalDateTime.now());
        batch.setQuantityProduced(outputQuantity);
        batch.setQualityGrade(qualityGrade);
        
        productionBatchRepository.save(batch);

        // Add to Inventory (We construct a product name based on material or just a generic one for now)
        // In a real system, you might select a target product ID. 
        // Here we format it like "Product from [Material Name]" or just use a fixed mapping.
        String productName = "成品-" + batch.getMaterialUsed().getName();
        productStockService.addStock(productName, outputQuantity, "kg");
    }
}
