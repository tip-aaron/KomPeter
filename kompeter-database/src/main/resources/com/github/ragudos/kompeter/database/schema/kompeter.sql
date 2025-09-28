PRAGMA foreign_keys = ON;
PRAGMA journal_mode = WAL;
PRAGMA synchronous = NORMAL;
PRAGMA BUSY_TIMEOUT = 50000;

BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS roles (
    _role_id INTEGER PRIMARY KEY AUTOINCREMENT,
    role_name TEXT UNIQUE NOT NULL, -- admin, clerk, sales staff, inventory manager,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS accounts (
    _user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    contactNum VARCHAR(255) UNIQUE,
    is_verified BOOLEAN NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    verified_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS login_status (
    _user_id INTEGER NOT NULL PRIMARY KEY,
    failed_login_attempts INTEGER NOT NULL DEFAULT 0,
    locked_until TIMESTAMP NULL,
    is_locked BOOLEAN NOT NULL DEFAULT 0,
    FOREIGN KEY (_user_id) REFERENCES accounts(_user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_roles (
    _user_id INTEGER NOT NULL,
    _role_id INTEGER NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (_user_id, _role_id),
    FOREIGN KEY (_user_id) REFERENCES accounts(_user_id) ON DELETE CASCADE,
    FOREIGN KEY (_role_id) REFERENCES roles(_role_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
    _user_id INTEGER PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,    
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (_user_id) REFERENCES accounts(_user_id) ON DELETE CASCADE
);

-- user sessions
CREATE TABLE IF NOT EXISTS user_sessions (
    _session_id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(8)))),
    _user_id INTEGER NOT NULL,
    login_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    logout_time TIMESTAMP NULL,
    last_activity TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT 1,
    FOREIGN KEY (_user_id) REFERENCES accounts(_user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_logs (
    _user_log_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _session_id TEXT NOT NULL,
    _user_id INTEGER NOT NULL,
    user_action VARCHAR(255) NOT NULL,
    action_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    will_be_deleted_at TIMESTAMP NOT NULL DEFAULT (DATETIME('now', '+30 days')),
    ip_address VARCHAR(255),
    FOREIGN KEY (_user_id) REFERENCES accounts(_user_id) ON DELETE CASCADE,
    FOREIGN KEY (_session_id) REFERENCES user_sessions(_session_id) ON DELETE CASCADE
);

-- suppliers
CREATE TABLE IF NOT EXISTS suppliers(
    _supplier_id INTEGER PRIMARY KEY AUTOINCREMENT,
    supplier_name VARCHAR(255) UNIQUE NOT NULL,
    supplier_address VARCHAR(255) NOT NULL,
    contact_person VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,   
    updated_at TIMESTAMP NULL	
);

-- inventory categories
CREATE TABLE IF NOT EXISTS categories (
    _category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_name VARCHAR(255) UNIQUE NOT NULL,	 -- Laptop, Computer peripherals, Accessories
    description TEXT
);

-- types
CREATE TABLE IF NOT EXISTS types (
    _type_id INTEGER PRIMARY KEY AUTOINCREMENT,
    type_name VARCHAR(255) UNIQUE NOT NULL, -- GPU, CPU, RAM, SSD, HDD, Monitor, Keyboard, Mouse, Headset, Speaker, Microphone, Webcam, etc.
    description TEXT
);

CREATE TABLE IF NOT EXISTS category_types (
    _category_id INTEGER NOT NULL,
    _type_id INTEGER NOT NULL,
    PRIMARY KEY (_category_id, _type_id),
    FOREIGN KEY (_category_id) REFERENCES categories(_category_id) ON DELETE CASCADE,
    FOREIGN KEY (_type_id) REFERENCES types(_type_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS inventory (
    _item_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _type_id INTEGER NOT NULL, 
    item_name VARCHAR(255) NOT NULL,
    item_brand VARCHAR(255) NOT NULL,
    description TEXT,
    selling_price REAL NOT NULL CHECK (selling_price >= 0),
    item_quantity INTEGER NOT NULL CHECK (item_quantity >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (_type_id) REFERENCES types(_type_id) ON DELETE CASCADE
);

-- purchases (header)
CREATE TABLE IF NOT EXISTS purchases (
  _purchase_id INTEGER PRIMARY KEY AUTOINCREMENT,
  purchase_code TEXT NOT NULL UNIQUE DEFAULT (hex(randomblob(5))),
  _supplier_id INTEGER NOT NULL,
  purchase_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  delivered_date TIMESTAMP,
  total_amount REAL NOT NULL DEFAULT 0.0 CHECK (total_amount >= 0),
  status VARCHAR(50) NOT NULL DEFAULT 'pending',
  invoice_number VARCHAR(255),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  
  updated_at TIMESTAMP NULL,
  FOREIGN KEY(_supplier_id) REFERENCES suppliers(_supplier_id) ON DELETE CASCADE
);

-- purchase line items
CREATE TABLE IF NOT EXISTS purchase_items(
  _purchase_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
  _purchase_id INTEGER NOT NULL,
  _item_id INTEGER NOT NULL,
  item_brand VARCHAR(255) NOT NULL,
  qty_ordered INTEGER NOT NULL CHECK(qty_ordered > 0),
  unit_cost REAL NOT NULL CHECK(unit_cost >= 0),
  line_total REAL NOT NULL CHECK(line_total >= 0),
  qty_received INTEGER NOT NULL DEFAULT 0 CHECK(qty_received >= 0),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  
  FOREIGN KEY(_purchase_id) REFERENCES purchases(_purchase_id) ON DELETE CASCADE,
  FOREIGN KEY(_item_id) REFERENCES inventory(_item_id) ON DELETE CASCADE
);

-- POS (orders)
CREATE TABLE IF NOT EXISTS orders (
    _order_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _order_code TEXT NOT NULL UNIQUE DEFAULT (hex(randomblob(5))),
    customer_name VARCHAR(255),
    total_amount REAL NOT NULL DEFAULT 0.0 CHECK (total_amount >= 0),
    payment_method VARCHAR(255) NOT NULL DEFAULT 'cash',
    order_status VARCHAR(255) NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,    
    updated_at TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS order_items (
    _order_items_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _order_id INTEGER NOT NULL,
    _item_id INTEGER NOT NULL,
    _type_id INTEGER NOT NULL, 
    item_quantity INTEGER NOT NULL CHECK (item_quantity > 0),
    selling_price REAL NOT NULL CHECK (selling_price >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,    
    FOREIGN KEY (_order_id) REFERENCES orders(_order_id) ON DELETE CASCADE,
    FOREIGN KEY (_type_id) REFERENCES types(_type_id) ON DELETE CASCADE, -- Reference the new 'types' table
    FOREIGN KEY (_item_id) REFERENCES inventory(_item_id) ON DELETE CASCADE
);
-- indexes
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_accounts_email ON accounts(email);
CREATE INDEX IF NOT EXISTS idx_items_name ON inventory(item_name);
CREATE INDEX IF NOT EXISTS idx_purchases_supplier ON purchases(_supplier_id);

COMMIT;
