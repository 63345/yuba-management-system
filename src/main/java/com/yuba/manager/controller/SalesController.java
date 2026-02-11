package com.yuba.manager.controller;

import com.yuba.manager.entity.SalesOrder;
import com.yuba.manager.service.ProductStockService;
import com.yuba.manager.service.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/sales")
public class SalesController {

    private final SalesService salesService;
    private final ProductStockService productStockService;

    public SalesController(SalesService salesService, ProductStockService productStockService) {
        this.salesService = salesService;
        this.productStockService = productStockService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", salesService.findAll());
        model.addAttribute("stocks", productStockService.findAll());
        model.addAttribute("activePage", "sales");
        return "sales";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute SalesOrder order) {
        salesService.createOrder(order);
        return "redirect:/sales";
    }

    @PostMapping("/ship")
    public String shipOrder(@RequestParam Long orderId, 
                            @RequestParam Long productStockId, 
                            @RequestParam Double quantity) {
        try {
            salesService.shipOrder(orderId, productStockId, quantity);
        } catch (Exception e) {
             return "redirect:/sales?error=" + e.getMessage();
        }
        return "redirect:/sales";
    }
}
