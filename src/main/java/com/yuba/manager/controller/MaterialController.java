package com.yuba.manager.controller;

import com.yuba.manager.entity.Material;
import com.yuba.manager.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("materials", materialService.findAll());
        model.addAttribute("activePage", "materials");
        return "materials";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Material material) {
        // Simple logic: if ID exists update, else create
        // Initial stock can be 0 if not provided
        if (material.getCurrentStock() == null) {
            material.setCurrentStock(0.0);
        }
        materialService.save(material);
        return "redirect:/materials";
    }
    
    @PostMapping("/addStock")
    public String addStock(@RequestParam Long id, @RequestParam Double quantity) {
        materialService.addStock(id, quantity);
        return "redirect:/materials";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        materialService.delete(id);
        return "redirect:/materials";
    }
}
