-- This script seeds the provided SQLite schema with sample data.
-- It follows the logical flow of the database, ensuring that tables with foreign key constraints
-- are populated in the correct order.

PRAGMA foreign_keys = ON;
PRAGMA journal_mode = WAL;
PRAGMA synchronous = NORMAL;
PRAGMA BUSY_TIMEOUT = 50000;

BEGIN TRANSACTION;

-- Roles
INSERT INTO roles (_role_id, role_name, description) VALUES
(1, 'admin', 'Full system access and management privileges'),
(2, 'manager', 'Manages staff, inventory, and purchases'),
(3, 'cashier', 'Processes point-of-sale transactions'),
(4, 'inventory clerk', 'Manages and updates inventory records'),
(5, 'auditor', 'Performs financial and stock audits'),
(6, 'supplier', 'A representative for a supplier company');

-- Accounts
INSERT INTO accounts (_user_id, password_hash, email, contactNum, is_verified, created_at, verified_at) VALUES
(1, 'peter_hash_123', 'peter@example.com', '09171234567', 1, '2025-09-20 08:00:00', '2025-09-20 08:05:00'),
(2, 'hanz_hash_456', 'hanz@example.com', '09181234567', 1, '2025-09-20 08:01:00', '2025-09-20 08:06:00'),
(3, 'jerick_hash_789', 'jerick@example.com', '09191234567', 1, '2025-09-20 08:02:00', '2025-09-20 08:07:00'),
(4, 'aaron_hash_abc', 'aaron@example.com', '09201234567', 1, '2025-09-20 08:03:00', '2025-09-20 08:08:00'),
(5, 'kurt_hash_def', 'kurt@example.com', '09211234567', 1, '2025-09-20 08:04:00', '2025-09-20 08:09:00'),
(6, 'supplier_hash_xyz', 'supplier@example.com', '09221234567', 1, '2025-09-20 08:05:00', '2025-09-20 08:10:00');

-- Users
INSERT INTO users (_user_id, username, firstName, lastName) VALUES
(1, 'Peter', 'Peter', 'Parker'),
(2, 'Hanz', 'Hanz', 'Zimmer'),
(3, 'Jerick', 'Jerick', 'Serrano'),
(4, 'Aaron', 'Aaron', 'Cruz'),
(5, 'Kurt', 'Kurt', 'Cobain');

-- Login Status
INSERT INTO login_status (_user_id, failed_login_attempts) VALUES
(1, 0), (2, 0), (3, 0), (4, 0), (5, 0), (6, 0);

-- User Roles
INSERT INTO user_roles (_user_id, _role_id) VALUES
(1, 1), -- Peter (admin)
(2, 2), -- Hanz (manager)
(2, 3), -- Hanz (cashier)
(3, 3), -- Jerick (cashier)
(4, 4), -- Aaron (inventory clerk)
(5, 5), -- Kurt (auditor)
(6, 6); -- Supplier account

-- Suppliers
INSERT INTO suppliers (_supplier_id, supplier_name, supplier_address, contact_person, email, phone, description) VALUES
(1, 'Tech Solutions Inc.', '123 Tech Avenue, Cityville', 'Jane Doe', 'info@techsolutions.com', '123-456-7890', 'Major supplier of computer components.'),
(2, 'Gadget Global', '456 Gadget Road, Townsville', 'John Smith', 'sales@gadgetglobal.net', '987-654-3210', 'Wholesaler for computer peripherals and accessories.'),
(3, 'Component Co.', '789 Component Blvd, Villagetown', 'Alice Johnson', 'contact@componentco.org', '555-123-4567', 'Specializes in internal PC parts.'),
(4, 'Peripheral Paradise', '101 Periph Lane, Suburbia', 'Bob Brown', 'orders@peripheralparadise.com', '555-987-6543', 'Supplier of high-end keyboards and mice.');

-- Categories
INSERT INTO categories (_category_id, category_name) VALUES
(1, 'Laptop'),
(2, 'Computer peripherals'),
(3, 'Accessories'),
(4, 'PC Components');

-- Types
INSERT INTO types (_type_id, type_name) VALUES
(1, 'Laptop'),
(2, 'GPU'),
(3, 'CPU'),
(4, 'RAM'),
(5, 'SSD'),
(6, 'HDD'),
(7, 'Monitor'),
(8, 'Keyboard'),
(9, 'Mouse'),
(10, 'Headset'),
(11, 'Speaker'),
(12, 'Microphone'),
(13, 'Webcam'),
(14, 'Motherboard'),
(15, 'Power Supply'),
(16, 'Case');

-- Category_Types
INSERT INTO category_types (_category_id, _type_id) VALUES
(1, 1), -- Laptop -> Laptop
(2, 7), -- Computer peripherals -> Monitor
(2, 8), -- Computer peripherals -> Keyboard
(2, 9), -- Computer peripherals -> Mouse
(3, 10), -- Accessories -> Headset
(3, 11), -- Accessories -> Speaker
(3, 12), -- Accessories -> Microphone
(3, 13), -- Accessories -> Webcam
(4, 2), -- PC Components -> GPU
(4, 3), -- PC Components -> CPU
(4, 4), -- PC Components -> RAM
(4, 5), -- PC Components -> SSD
(4, 6), -- PC Components -> HDD
(4, 14), -- PC Components -> Motherboard
(4, 15), -- PC Components -> Power Supply
(4, 16); -- PC Components -> Case

-- Inventory (Product Master)
INSERT INTO inventory (_item_id, _type_id, item_name, item_brand, description, selling_price, item_quantity) VALUES
(1, 2, 'GeForce RTX 4080', 'NVIDIA', 'High-end graphics card for gaming and professional tasks.', 75000.00, 15),
(2, 3, 'Ryzen 9 7950X', 'AMD', '16-core CPU for high-performance computing.', 35000.00, 20),
(3, 8, 'G Pro X TKL', 'Logitech', 'Tenkeyless gaming keyboard with mechanical switches.', 9000.00, 50),
(4, 7, 'Odyssey G9', 'Samsung', '49-inch curved gaming monitor.', 60000.00, 10),
(5, 5, '990 PRO 2TB', 'Samsung', 'High-speed M.2 NVMe SSD for fast storage.', 11500.00, 30);

-- Purchases
INSERT INTO purchases (_purchase_id, _supplier_id, purchase_code, purchase_date, delivered_date, total_amount, status, invoice_number) VALUES
(1, 1, 'PCH12345', '2025-09-22 10:00:00', '2025-09-23 15:00:00', 150000.00, 'delivered', 'INV-001'),
(2, 2, 'PCH67890', '2025-09-22 11:30:00', '2025-09-24 10:00:00', 90000.00, 'delivered', 'INV-002'),
(3, 3, 'PCHABCDE', '2025-09-23 09:00:00', NULL, 70000.00, 'pending', 'INV-003'),
(4, 4, 'PCHFGHIJ', '2025-09-24 14:00:00', NULL, 120000.00, 'pending', 'INV-004');

-- Purchase Items
INSERT INTO purchase_items (_purchase_id, _item_id, item_brand, qty_ordered, unit_cost, line_total, qty_received) VALUES
(1, 1, 'NVIDIA', 2, 70000.00, 140000.00, 2), -- 2 RTX 4080s
(1, 2, 'AMD', 1, 35000.00, 35000.00, 1), -- 1 Ryzen 9
(2, 3, 'Logitech', 10, 8500.00, 85000.00, 10), -- 10 G Pro X Keyboards
(2, 4, 'Samsung', 1, 58000.00, 58000.00, 1), -- 1 Odyssey Monitor
(3, 5, 'Samsung', 5, 11000.00, 55000.00, 0), -- 5 990 PRO SSDs
(4, 3, 'Logitech', 5, 8500.00, 42500.00, 0); -- 5 G Pro X Keyboards

-- Orders
INSERT INTO orders (_order_id, _order_code, customer_name, total_amount, payment_method, order_status) VALUES
(1, 'ORD111', 'Lianne', 21000.00, 'cash', 'completed'),
(2, 'ORD222', 'Kyle', 75000.00, 'completed'),
(3, 'ORD333', 'James', 9000.00, 'cash', 'completed'),
(4, 'ORD444', 'Hannah', 11500.00, 'online payment', 'pending');

-- Order Items
INSERT INTO order_items (_order_id, _item_id, _type_id, item_quantity, selling_price) VALUES
(1, 3, 8, 1, 9000.00), -- 1 G Pro X Keyboard
(1, 5, 5, 1, 11500.00), -- 1 990 PRO SSD
(2, 1, 2, 1, 75000.00), -- 1 RTX 4080
(3, 3, 8, 1, 9000.00), -- 1 G Pro X Keyboard
(4, 5, 5, 1, 11500.00); -- 1 990 PRO SSD

COMMIT;