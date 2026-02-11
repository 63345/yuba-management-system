package com.yuba.manager.controller;

import com.yuba.manager.service.MaterialService;
import com.yuba.manager.service.ProductionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/production")
public class ProductionController {

    private final ProductionService productionService;
    private final MaterialService materialService;

    public ProductionController(ProductionService productionService, MaterialService materialService) {
        this.productionService = productionService;
        this.materialService = materialService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("batches", productionService.findAll());
        model.addAttribute("materials", materialService.findAll());
        model.addAttribute("activePage", "production");
        return "production";
    }

    @PostMapping("/start")
    public String startBatch(@RequestParam String batchCode, 
                             @RequestParam Long materialId, 
                             @RequestParam Double materialQuantity) {
        try {
            productionService.startBatch(batchCode, materialId, materialQuantity);
        } catch (Exception e) {
            return "redirect:/production?error=" + e.getMessage();
        }
        return "redirect:/production";
    }

    @PostMapping("/complete")
    public String completeBatch(@RequestParam Long batchId, 
                                @RequestParam Double outputQuantity,
                                @RequestParam String qualityGrade) {
        productionService.completeBatch(batchId, outputQuantity, qualityGrade);
        return "redirect:/production";
    }
}
