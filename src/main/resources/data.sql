-- Admin: email = admin@company.com , password = admin123
INSERT INTO employees (email, full_name, password, role, department, phone, status, created_at)
VALUES ('admin@company.com', 'System Admin', '$2a$12$z8Y8e8t8f8g8h8j8k8l8m8n8o8p8q8r8s8t8u8v8w8x8y8z8A8B8C', 'ADMIN', 'IT', '9999999999', 'APPROVED', NOW())
ON CONFLICT (email) DO NOTHING;

-- Sample Assets
INSERT INTO assets (serial_number, type, brand, model, specifications, purchase_date, warranty_expiry, status, created_at)
VALUES
('LAP001', 'LAPTOP', 'Dell', 'XPS 15', 'i7, 32GB, 1TB SSD', '2024-01-15', '2027-01-15', 'AVAILABLE', NOW()),
('MON001', 'MONITOR', 'Samsung', '49" Odyssey', '5120x1440', '2024-02-20', '2026-02-20', 'AVAILABLE', NOW()),
('MOUSE001', 'MOUSE', 'Logitech', 'MX Master 3S', 'Wireless', '2024-03-10', NULL, 'AVAILABLE', NOW())
ON CONFLICT (serial_number) DO NOTHING;
