package com.yuba.manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "sales_orders")
@Data
public class SalesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String orderContent; // Simple string description for now, e.g. "10kg Yuba"
    private Double totalAmount;
    private String status; // PENDING, SHIPPED, COMPLETED, CANCELLED
    private LocalDateTime orderTime;
}
