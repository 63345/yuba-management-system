package com.yuba.manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "production_batches")
@Data
public class ProductionBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String batchCode; // e.g. "BATCH-20231027-001"
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    // Status: PLANNED, PROCESSING, COMPLETED
    private String status; 

    @ManyToOne
    private Material materialUsed;
    
    private Double materialQuantityUsed;
    
    // Output
    private Double quantityProduced;
    private String qualityGrade; // "A", "B", "C"
}
