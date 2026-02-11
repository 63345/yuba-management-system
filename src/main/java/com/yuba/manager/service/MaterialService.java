package com.yuba.manager.service;

import com.yuba.manager.entity.Material;
import com.yuba.manager.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    @Transactional
    public Material save(Material material) {
        return materialRepository.save(material);
    }
    
    @Transactional
    public void addStock(Long id, Double quantity) {
        Material material = materialRepository.findById(id).orElseThrow();
        material.setCurrentStock(material.getCurrentStock() + quantity);
        materialRepository.save(material);
    }

    @Transactional
    public void delete(Long id) {
        materialRepository.deleteById(id);
    }
}
