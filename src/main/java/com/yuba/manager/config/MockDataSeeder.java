package com.yuba.manager.config;

import com.yuba.manager.entity.*;
import com.yuba.manager.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class MockDataSeeder {

    private final Random random = new Random();

    @Bean
    public CommandLineRunner initMockData(
            UserRepository userRepository,
            MaterialRepository materialRepository,
            ProductStockRepository productStockRepository,
            ProductionBatchRepository productionBatchRepository,
            SalesOrderRepository salesOrderRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            seedMaterials(materialRepository);
            seedProductStocks(productStockRepository);
            seedProductionBatches(productionBatchRepository, materialRepository);
            seedSalesOrders(salesOrderRepository);
            seedExtraUsers(userRepository, passwordEncoder);
            
            System.out.println("中文模拟数据填充完成 (Mock data seeding completed)!");
        };
    }

    private void seedMaterials(MaterialRepository repository) {
        if (repository.count() > 0) return;

        List<Material> materials = new ArrayList<>();
        String[] names = {"优质非转基因大豆", "有机黑豆", "纯净过滤水", "食用消泡剂", "葡萄糖酸内酯(凝固剂)"};
        String[] types = {"原材料", "原材料", "辅料", "添加剂", "添加剂"};
        String[] units = {"千克", "千克", "升", "升", "千克"};

        for (int i = 0; i < 20; i++) {
            Material m = new Material();
            int idx = i % names.length;
            m.setName(names[idx] + " #" + (i + 1));
            m.setType(types[idx]);
            m.setUnit(units[idx]);
            m.setCurrentStock(100.0 + random.nextInt(900)); // 100-1000
            materials.add(m);
        }
        repository.saveAll(materials);
        System.out.println("已填充 " + materials.size() + " 条原料数据。");
    }

    private void seedProductStocks(ProductStockRepository repository) {
        if (repository.count() > 0) return;

        List<ProductStock> products = new ArrayList<>();
        String[] names = {"精品腐竹", "鲜豆皮", "手工豆结", "油炸腐竹"};
        
        for (int i = 0; i < 50; i++) {
            ProductStock p = new ProductStock();
            p.setProductName(names[random.nextInt(names.length)]);
            p.setQuantity(50.0 + random.nextInt(450));
            p.setUnit("千克");
            p.setWarehouseLocation((char)('A' + random.nextInt(5)) + "区-" + (random.nextInt(10) + 1) + "号货架");
            products.add(p);
        }
        repository.saveAll(products);
        System.out.println("已填充 " + products.size() + " 条库存数据。");
    }

    private void seedProductionBatches(ProductionBatchRepository batchRepo, MaterialRepository materialRepo) {
        if (batchRepo.count() > 0) return;

        List<Material> materials = materialRepo.findAll();
        if (materials.isEmpty()) return;

        List<ProductionBatch> batches = new ArrayList<>();
        // 使用中文状态，或者根据您前端需求保持英文。这里改为中文演示。
        String[] statuses = {"计划中", "生产中", "已完成", "已完成", "已完成"};
        String[] grades = {"一级", "二级", "三级", "特级", "一级"};

        for (int i = 0; i < 50; i++) {
            ProductionBatch b = new ProductionBatch();
            b.setBatchCode("PH-" + (20240000 + i)); // PH = 批号
            b.setStartTime(LocalDateTime.now().minusDays(random.nextInt(30)));
            b.setEndTime(b.getStartTime().plusHours(4 + random.nextInt(8)));
            
            String status = statuses[random.nextInt(statuses.length)];
            b.setStatus(status);
            
            b.setMaterialUsed(materials.get(random.nextInt(materials.size())));
            b.setMaterialQuantityUsed(50.0 + random.nextInt(150));
            
            if ("已完成".equals(status)) {
                b.setQuantityProduced(b.getMaterialQuantityUsed() * 0.6); // 60% 出品率
                b.setQualityGrade(grades[random.nextInt(grades.length)]);
            } else {
                b.setQuantityProduced(0.0);
            }
            batches.add(b);
        }
        batchRepo.saveAll(batches);
        System.out.println("已填充 " + batches.size() + " 条生产批次数据。");
    }

    private void seedSalesOrders(SalesOrderRepository repository) {
        if (repository.count() > 0) return;

        List<SalesOrder> orders = new ArrayList<>();
        String[] customers = {"老王私房菜", "便民超市", "城南批发市场", "云海大酒店", "好邻居小卖部"};
        String[] statuses = {"待处理", "已发货", "已完成", "已取消"};

        for (int i = 0; i < 100; i++) {
            SalesOrder o = new SalesOrder();
            o.setCustomerName(customers[random.nextInt(customers.length)]);
            o.setOrderContent("腐竹采购订单 #" + (i + 1));
            o.setTotalAmount(1000.0 + random.nextInt(9000));
            o.setStatus(statuses[random.nextInt(statuses.length)]);
            o.setOrderTime(LocalDateTime.now().minusDays(random.nextInt(60)));
            orders.add(o);
        }
        repository.saveAll(orders);
        System.out.println("已填充 " + orders.size() + " 条销售订单数据。");
    }

    private void seedExtraUsers(UserRepository repository, PasswordEncoder passwordEncoder) {
        if (repository.count() > 2) return; 

        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            User u = new User();
            u.setUsername("gongren" + i); // 工人1~10
            u.setPassword(passwordEncoder.encode("123456"));
            u.setRole("WORKER");
            users.add(u);
        }
        repository.saveAll(users);
        System.out.println("已填充 " + users.size() + " 个额外用户账号。");
    }
}
