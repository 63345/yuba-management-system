-- Create Database
CREATE DATABASE IF NOT EXISTS yuba_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE yuba_db;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Materials Table
CREATE TABLE IF NOT EXISTS materials (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    type VARCHAR(255),
    current_stock DOUBLE,
    unit VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Product Stocks Table
CREATE TABLE IF NOT EXISTS product_stocks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255),
    quantity DOUBLE,
    unit VARCHAR(50),
    warehouse_location VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Production Batches Table
CREATE TABLE IF NOT EXISTS production_batches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_code VARCHAR(100),
    start_time DATETIME,
    end_time DATETIME,
    status VARCHAR(50),
    material_used_id BIGINT,
    material_quantity_used DOUBLE,
    quantity_produced DOUBLE,
    quality_grade VARCHAR(50),
    CONSTRAINT fk_production_material FOREIGN KEY (material_used_id) REFERENCES materials(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Sales Orders Table
CREATE TABLE IF NOT EXISTS sales_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255),
    order_content TEXT,
    total_amount DOUBLE,
    status VARCHAR(50),
    order_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Optional: Insert initial admin user (This is plain text 'admin123', use with caution as app expects BCrypt)
-- INSERT INTO users (username, password, role) VALUES ('admin', '$2a$10$w... (encrypted password)', 'ADMIN');
-- Note: The application's DataInitializer.java will automatically create an admin user with an encrypted password if the table is empty.
