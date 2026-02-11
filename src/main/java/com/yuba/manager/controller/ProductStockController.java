package com.yuba.manager.controller;

import com.yuba.manager.service.ProductStockService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inventory")
public class ProductStockController {

    private final ProductStockService productStockService;

    public ProductStockController(ProductStockService productStockService) {
        this.productStockService = productStockService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("stocks", productStockService.findAll());
        model.addAttribute("activePage", "inventory");
        return "inventory";
    }
}
