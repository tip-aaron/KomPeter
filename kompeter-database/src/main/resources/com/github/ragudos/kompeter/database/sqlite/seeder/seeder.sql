-- SQLite Seeding Script

-- ===============================================
-- 1. USERS, ROLES, and ACCOUNTS
-- ===============================================

-- Roles (At least 4 rows)
INSERT INTO roles (_role_id, role_name, description) VALUES
(1, 'admin', 'Full system access and management privileges.'),
(2, 'manager', 'Manages staff, inventory, and purchases.'),
(3, 'cashier', 'Processes point-of-sale transactions.'),
(4, 'inventory clerk', 'Manages and updates inventory records.'),
(5, 'auditor', 'Performs financial and stock audits.'),
(6, 'supplier', 'External role for supplier data, not a direct system user.');


-- Users (At least 5 rows for requested users)
INSERT INTO users (_user_id, display_name, first_name, last_name) VALUES
(1, 'Peter', 'Peter', 'Parker'),
(2, 'Hanz', 'Hanz', 'Zimmer'),
(3, 'Jerick', 'Jerick', 'Serrano'),
(4, 'Aaron', 'Aaron', 'Cruz'),
(5, 'Kurt', 'Kurt', 'Cobain');


-- Accounts (At least 5 rows)
-- Note: Dummy hashes and salts are used.
INSERT INTO accounts (_account_id, _user_id, password_hash, password_salt, email) VALUES
(1, 1, 'hash_peter_admin', 'salt_peter', 'peter.admin@example.com'),
(2, 2, 'hash_hanz_manager', 'salt_hanz', 'hanz.manager@example.com'),
(3, 3, 'hash_jerick_cashier', 'salt_jerick', 'jerick.cashier@example.com'),
(4, 4, 'hash_aaron_clerk', 'salt_aaron', 'aaron.clerk@example.com'),
(5, 5, 'hash_kurt_auditor', 'salt_kurt', 'kurt.auditor@example.com');


-- User Roles (Assigning multiple roles to Hanz)
-- Note: user_roles table uses _user_id as PRIMARY KEY, implying a 1-to-1 relationship with the main role.
-- To represent multiple roles, we must assign one primary role per user or re-interpret the schema.
-- Assuming the primary role is based on the most senior/active role needed for seeding.
INSERT INTO user_roles (_user_id, _role_id) VALUES
(1, 1), -- Peter (admin)
(2, 2), -- Hanz (Manager) - Primary Role
(3, 3), -- Jerick (Cashier)
(4, 4), -- Aaron (Inventory Clerk)
(5, 5); -- Kurt (Auditor)


-- Sessions (At least 4 rows)
INSERT INTO sessions (_user_id, session_token, ip_address) VALUES
(1, 'token-peter-admin-001', '192.168.1.10'),
(2, 'token-hanz-manager-002', '192.168.1.11'),
(3, 'token-jerick-cashier-003', '10.0.0.5'),
(4, 'token-aaron-clerk-004', '10.0.0.6');

-- ===============================================
-- 2. INVENTORY SETUP
-- ===============================================

-- Item Categories (At least 4 rows)
INSERT INTO item_categories (_item_category_id, name, description) VALUES
(1, 'PC Components', 'Internal parts like CPU, GPU, RAM, etc.'),
(2, 'Peripherals', 'External devices like Keyboard, Mouse, Monitor.'),
(3, 'Laptops', 'Complete portable computer systems.'),
(4, 'Accessories', 'Cables, cleaning kits, mousepads, etc.');


-- Item Brands (At least 4 rows)
INSERT INTO item_brands (_item_brand_id, name, description) VALUES
(1, 'Logitech', 'Leading brand for computer peripherals.'),
(2, 'Kingston', 'Specializing in memory products.'),
(3, 'AMD', 'Manufacturer of CPUs and GPUs.'),
(4, 'ASUS', 'Manufacturer of motherboards, laptops, and GPUs.'),
(5, 'Samsung', 'Brand for storage devices and monitors.');


-- Items (At least 4 rows)
INSERT INTO items (_item_id, name, description) VALUES
(1, 'HyperX Fury 16GB RAM', '16GB DDR4 RAM stick.'),
(2, 'Ryzen 5 7600X CPU', 'Mid-range desktop processor.'),
(3, 'G Pro X Mechanical Keyboard', 'Tenkeyless mechanical gaming keyboard.'),
(4, 'ZenBook 14 Laptop', 'Slim and portable 14-inch laptop.'),
(5, 'Samsung 990 Pro 2TB SSD', 'High-speed 2TB NVMe M.2 drive.');


-- Item Category Assignments (At least 4 rows)
INSERT INTO item_category_assignments (_item_id, _item_category_id) VALUES
(1, 1), -- RAM is a PC Component
(2, 1), -- CPU is a PC Component
(3, 2), -- Keyboard is a Peripheral
(4, 3), -- ZenBook is a Laptop
(5, 1); -- SSD is a PC Component


-- Item Stocks (At least 4 rows)
INSERT INTO item_stocks (_item_stock_id, _item_id, _item_brand_id, unit_price_php, quantity, minimum_quantity) VALUES
(1, 1, 2, 3500.00, 100, 20),  -- HyperX RAM (Kingston)
(2, 2, 3, 12000.00, 50, 10),   -- Ryzen 5 (AMD)
(3, 3, 1, 8500.00, 75, 15),    -- G Pro X Keyboard (Logitech)
(4, 4, 4, 45000.00, 20, 5),   -- ZenBook 14 (ASUS)
(5, 5, 5, 12500.00, 40, 8);  -- 990 Pro SSD (Samsung)


-- Item Restocks (At least 4 rows)
-- Initial stock counts are based on the inserted data above.
INSERT INTO item_restocks (_item_stock_id, quantity_before, quantity_after, quantity_added) VALUES
(1, 0, 100, 100), -- Initial stock of RAM
(2, 0, 50, 50),   -- Initial stock of CPU
(3, 0, 75, 75),   -- Initial stock of Keyboard
(1, 100, 150, 50); -- Additional restock for RAM

-- ===============================================
-- 3. PURCHASES (Restocking)
-- ===============================================

-- Suppliers (At least 4 rows, no link to the 'supplier' role yet)
INSERT INTO suppliers (_supplier_id, name, email, street, city, state, postal_code, country) VALUES
(1, 'Component Distributors Inc.', 'contact@compdist.com', '123 Tech St', 'Makati', 'Metro Manila', '1209', 'Philippines'),
(2, 'Peripheral World', 'sales@periworld.net', '456 Gadget Ave', 'Quezon City', 'Metro Manila', '1103', 'Philippines'),
(3, 'ASUS PH Partner', 'orders@asusph.com', '789 Laptop Blvd', 'Manila', 'Metro Manila', '1008', 'Philippines'),
(4, 'Storage Solutions', 'support@storage.com', '101 Drive Rd', 'Cebu City', 'Cebu', '6000', 'Philippines');


-- Purchases (At least 4 rows)
INSERT INTO purchases (_purchase_id, _supplier_id, purchase_date, purchase_code, delivery_date, vat_percent, discount_value, discount_type) VALUES
(1, 1, DATETIME('now', '-3 days'), 'PO-CDI-2025-001', DATETIME('now', '-2 days'), 0.12, 500.00, 'fixed'),
(2, 2, DATETIME('now', '-4 days'), 'PO-PW-2025-002', DATETIME('now', '-3 days'), 0.12, 0.05, 'percentage'),
(3, 3, DATETIME('now', '-2 days'), 'PO-APP-2025-003', NULL, 0.12, NULL, NULL), -- Pending Delivery
(4, 4, DATETIME('now', '-1 day'), 'PO-SS-2025-004', DATETIME('now'), 0.12, 0, 'fixed');


-- Purchase Payments (At least 4 rows)
INSERT INTO purchase_payments (_purchse_id, payment_date, reference_number, payment_method, amount_php) VALUES
(1, DATETIME('now', '-2 days', '+1 hour'), 'REF-PP-98765', 'bank_transfer', 69500.00), -- Total Cost 70000 - 500
(2, DATETIME('now', '-3 days', '+2 hours'), 'REF-PP-65432', 'cash', 85500.00), -- Total Cost 90000 - 5% (4500)
(3, DATETIME('now', '-2 days', '+3 hours'), 'REF-PP-32109', 'gcash', 45000.00), -- Partial Payment
(4, DATETIME('now'), 'REF-PP-09876', 'bank_transfer', 125000.00);


-- Purchase Item Stocks (At least 4 rows)
-- Note: Unit cost here is the supplier's price, not the selling price in item_stocks
INSERT INTO purchase_item_stocks (_purchase_id, _item_stock_id, quantity_ordered, quantity_received, unit_cost_php) VALUES
(1, 2, 5, 5, 10000.00), -- 5 CPUs @ 10000. Total: 50000
(1, 1, 20, 20, 1000.00), -- 20 RAMs @ 1000. Total: 20000. Purchase 1 total: 70000
(2, 3, 10, 10, 8000.00), -- 10 Keyboards @ 8000. Total: 80000
(2, 5, 1, 1, 10000.00), -- 1 SSD @ 10000. Total: 10000. Purchase 2 total: 90000
(3, 4, 1, 0, 40000.00), -- 1 ZenBook @ 40000. (Not yet received)
(4, 5, 10, 10, 12500.00); -- 10 SSDs @ 12500. Total: 125000

-- ===============================================
-- 4. SALES (POS Transactions)
-- ===============================================

-- Sales (At least 4 rows)
INSERT INTO sales (_sale_id, sale_date, sale_code, customer_name, vat_percent, discount_value, discount_type) VALUES
(1, DATETIME('now', '+1 hour'), 'SALE-2025-0001', 'Client A', 0.12, 0, 'fixed'),
(2, DATETIME('now', '+2 hours'), 'SALE-2025-0002', 'Client B', 0.12, 0.10, 'percentage'),
(3, DATETIME('now', '+3 hours'), 'SALE-2025-0003', 'Walk-in', 0.12, 100.00, 'fixed'),
(4, DATETIME('now', '+4 hours'), 'SALE-2025-0004', 'Client D', 0.12, NULL, NULL);

-- Sale Item Stocks (At least 4 rows)
INSERT INTO sale_item_stocks (_sale_id, _item_stock_id, quantity, unit_price_php) VALUES
(1, 1, 2, 3500.00), -- 2 RAMs @ 3500. Total: 7000
(1, 3, 1, 8500.00), -- 1 Keyboard @ 8500. Total: 8500. Sale 1 Subtotal: 15500
(2, 2, 1, 12000.00), -- 1 CPU @ 12000. Sale 2 Subtotal: 12000
(3, 3, 1, 8500.00), -- 1 Keyboard @ 8500. Sale 3 Subtotal: 8500
(4, 5, 1, 12500.00); -- 1 SSD @ 12500. Sale 4 Subtotal: 12500

-- Sale Payments (At least 4 rows)
INSERT INTO sale_payments (_sale_id, payment_date, reference_number, payment_method, amount_php) VALUES
(1, DATETIME('now', '+1 hour', '+5 minutes'), NULL, 'cash', 15500.00 * 1.12), -- Full payment including VAT
(2, DATETIME('now', '+2 hours', '+5 minutes'), 'GCASH-12345678', 'gcash', (12000.00 * 0.90) * 1.12), -- Full payment including VAT and 10% discount
(3, DATETIME('now', '+3 hours', '+5 minutes'), NULL, 'cash', (8500.00 - 100.00) * 1.12), -- Full payment including VAT and 100 fixed discount
(4, DATETIME('now', '+4 hours', '+5 minutes'), 'BTRANS-998877', 'bank_transfer', 12500.00 * 1.12); -- Full payment including VAT

