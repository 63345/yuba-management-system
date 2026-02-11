package com.yuba.manager.service;

import com.yuba.manager.entity.ProductStock;
import com.yuba.manager.entity.SalesOrder;
import com.yuba.manager.repository.ProductStockRepository;
import com.yuba.manager.repository.SalesOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalesService {

    private final SalesOrderRepository salesOrderRepository;
    private final ProductStockRepository productStockRepository;

    public SalesService(SalesOrderRepository salesOrderRepository, ProductStockRepository productStockRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.productStockRepository = productStockRepository;
    }

    public List<SalesOrder> findAll() {
        return salesOrderRepository.findAll();
    }

    @Transactional
    public void createOrder(SalesOrder order) {
        order.setOrderTime(LocalDateTime.now());
        order.setStatus("PENDING");
        salesOrderRepository.save(order);
    }

    @Transactional
    public void shipOrder(Long orderId, Long productStockId, Double quantity) {
        SalesOrder order = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("Order already processed");
        }

        ProductStock stock = productStockRepository.findById(productStockId)
                .orElseThrow(() -> new RuntimeException("Product stock not found"));
        
        if (stock.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient product stock");
        }

        stock.setQuantity(stock.getQuantity() - quantity);
        productStockRepository.save(stock);

        order.setStatus("SHIPPED");
        salesOrderRepository.save(order);
    }
    
    @Transactional
    public void completeOrder(Long orderId) {
         SalesOrder order = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
         order.setStatus("COMPLETED");
         salesOrderRepository.save(order);
    }
    
    @Transactional
    public void cancelOrder(Long orderId) {
         SalesOrder order = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
         order.setStatus("CANCELLED");
         salesOrderRepository.save(order);
    }
}
