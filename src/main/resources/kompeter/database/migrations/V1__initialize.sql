--
-- ALL PRICES AND COSTS ARE VAT INCLUSIVE, SO TO GET
-- THE NET COST OR PRICE, DEDUCT THE VAT VALUE
--
-- ========================================================= --
-- =====                                             ======= --
-- =====                  SCHEMAS                    ======= --
-- =====                                             ======= --
-- ========================================================= --
CREATE TABLE IF NOT EXISTS roles (
    _role_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name TEXT NOT NULL UNIQUE,
    description TEXT NOT NULL DEFAULT ''
);
CREATE TABLE IF NOT EXISTS users (
    _user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    display_name TEXT NOT NULL UNIQUE,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    display_image TEXT
);
CREATE TABLE IF NOT EXISTS user_roles (
    _user_id INTEGER NOT NULL,
    _role_id INTEGER NOT NULL,
    UNIQUE (_user_id, _role_id),
    PRIMARY KEY (_user_id, _role_id),
    FOREIGN KEY (_user_id) REFERENCES users (_user_id) ON DELETE CASCADE,
    FOREIGN KEY (_role_id) REFERENCES roles (_role_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS accounts (
    _account_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _user_id INTEGER NOT NULL,
    password_hash TEXT NOT NULL,
    password_salt TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    FOREIGN KEY (_user_id) REFERENCES users (_user_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS sessions (
    _session_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _user_id INTEGER NOT NULL,
    expires_at TIMESTAMP NOT NULL DEFAULT (DATETIME('now', '+1 hour')),
    session_token TEXT NOT NULL UNIQUE,
    FOREIGN KEY (_user_id) REFERENCES users (_user_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS product_categories (
    _product_category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name TEXT NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS product_brands (
    _product_brand_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name TEXT NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS products (
    _product_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _product_category_id INTEGER,
    _product_brand_id INTEGER,
    name TEXT NOT NULL UNIQUE,
    description TEXT NOT NULL DEFAULT '',
    markup_rate REAL NOT NULL,
    -- with markup rate already
    price REAL NOT NULL,
    -- with vat
    average_cost REAL NOT NULL,
    average_cost_vat_rate REAL NOT NULL,
    quantity_in_hand INTEGER NOT NULL DEFAULT 0,
    display_image TEXT NOT NULL DEFAULT '',
    is_active BOOLEAN DEFAULT false,
    is_deleted BOOLEAN DEFAULT false,
    FOREIGN KEY (_product_category_id) REFERENCES product_categories (_product_category_id) ON DELETE
    SET NULL,
        FOREIGN KEY (_product_brand_id) REFERENCES product_brands (_product_brand_id) ON DELETE
    SET NULL
);
CREATE TABLE IF NOT EXISTS suppliers (
    _supplier_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name TEXT NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS customers (
    _customer_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name TEXT NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS purchase_orders (
    _purchase_order_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _supplier_id INTEGER,
    purchase_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    purchase_code TEXT NOT NULL DEFAULT '',
    vat_rate REAL NOT NULL,
    FOREIGN KEY (_supplier_id) REFERENCES suppliers (_supplier_id) ON DELETE
    SET NULL
);
CREATE TABLE IF NOT EXISTS purchase_order_lines (
    _purchase_order_id INTEGER NOT NULL,
    _product_id INTEGER NOT NULL,
    unit_price REAL NOT NULL,
    quantity INTEGER NOT NULL,
    UNIQUE (_purchase_order_id, _product_id),
    PRIMARY KEY (_purchase_order_id, _product_id),
    FOREIGN KEY (_purchase_order_id) REFERENCES purchase_orders (_purchase_order_id) ON DELETE CASCADE,
    FOREIGN KEY (_product_id) REFERENCES products (_product_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS sales (
    _sale_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _customer_id INTEGER,
    sale_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sale_code TEXT NOT NULL DEFAULT '',
    vat_rate REAL NOT NULL,
    FOREIGN KEY (_customer_id) REFERENCES customers (_customer_id) ON DELETE
    SET NULL
);
CREATE TABLE IF NOT EXISTS sale_discounts (
    _sale_discount_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _sale_id INTEGER NOT NULL,
    amount REAL NOT NULL,
    discount_type TEXT,
    FOREIGN KEY (_sale_id) REFERENCES sales (_sale_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS sale_lines (
    _sale_id INTEGER NOT NULL,
    _product_id INTEGER NOT NULL,
    price REAL NOT NULL,
    quantity INTEGER NOT NULL,
    UNIQUE (_sale_id, _product_id),
    PRIMARY KEY (_sale_id, _product_id),
    FOREIGN KEY (_sale_id) REFERENCES sales (_sale_id) ON DELETE CASCADE,
    FOREIGN KEY (_product_id) REFERENCES products (_product_id) ON DELETE CASCADE
);
-- ========================================================= --
-- =====                                             ======= --
-- =====                   AUDIT                     ======= --
-- =====                                             ======= --
-- ========================================================= --
CREATE TABLE IF NOT EXISTS product_adjustments (
    _product_adjustment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _product_id INTEGER NOT NULL,
    _adjuster_id INTEGER,
    -- add user id, could provide ids for different systems that uses our app, but that's o ut of our scope
    adjustment_type TEXT NOT NULL CHECK (adjustment_type IN ('system', 'user')),
    quantiy_after INTEGER NOT NULL,
    quantity_before INTEGER NOT NULL,
    quantity_added INTEGER NOT NULL,
    FOREIGN KEY (_adjuster_id) REFERENCES users (_user_id) ON DELETE
    SET NULL
);
CREATE TABLE IF NOT EXISTS product_price_histories (
    _product_price_history_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _product_id INTEGER NOT NULL,
    from_date TIMESTAMP NOT NULL,
    markup_rate REAL NOT NULL,
    price REAL NOT NULL,
    FOREIGN KEY (_product_id) REFERENCES products (_product_id) ON DELETE CASCADE
);
-- ========================================================= --
-- =====                                             ======= --
-- =====                  VIRTUAL                    ======= --
-- =====                                             ======= --
-- ========================================================= --
CREATE VIRTUAL TABLE IF NOT EXISTS product_names_search USING fts5(_product_id, product_name);
CREATE VIRTUAL TABLE IF NOT EXISTS product_sales_search USING fts5(
    _sale_id,
    _product_id,
    product_name
);
CREATE VIRTUAL TABLE IF NOT EXISTS product_purchases_search USING fts5(
    _purchase_order_id,
    _product_id,
    product_name
);
-- ========================================================= --
-- =====                                             ======= --
-- =====                    VIEW                     ======= --
-- =====                                             ======= --
-- ========================================================= --
CREATE VIEW IF NOT EXISTS purchase_orders_view AS
SELECT po._purchase_order_id,
    po.purchase_code,
    po.purchase_date,
    po.vat_rate,
    COUNT(pol._product_id) AS total_products,
    SUM(pol.quantity) AS total_quantity,
    SUM(pol.unit_price) AS total_cost
FROM purchase_orders po
    INNER JOIN purchase_order_lines pol ON po._purchase_order_id = pol._purchase_order_id
GROUP BY po._purchase_order_id;
CREATE VIEW IF NOT EXISTS sales_view AS
SELECT s._sale_id,
    s.sale_code,
    s.sale_date,
    s.vat_rate,
    c.name AS customer_name,
    COUNT(sl._product_id) AS total_products,
    SUM(sl.quantity) AS total_quantity,
    SUM(sl.price) AS gross_price,
    SUM(sd.amount) AS discount_amount
FROM sales s
    INNER JOIN sale_lines sl ON s._sale_id = sl._sale_id
    INNER JOIN customers c ON c._customer_id = s._customer_id
GROUP BY s._sale_id,
    c._customer_id;
-- ========================================================= --
-- =====                                             ======= --
-- =====                 TRIGGERS                    ======= --
-- =====                                             ======= --
-- ========================================================= --