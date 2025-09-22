# Project Structure Guide

This project uses a **multi-module Maven structure**. The parent project (`KomPeter`) defines shared configurations, plugin versions, and dependency management. Each child module is responsible for its own domain.

```text
KomPeter/
├── pom.xml                  # Parent POM (packaging = pom)
├── kompeter-app-desktop/    # Desktop application (main entry point)
├── kompeter-assets/         # Helper classes for retrieving assets
├── kompeter-configuration/  # Helper classes for configurations
├── kompeter-core/           # Core business logic and domain models
├── kompeter-cryptography/   # Cryptographic utilities
├── kompeter-database/       # Database integration (JPA, JDBC, etc.)
├── kompeter-inventory/      # Inventory management logic
├── kompeter-lookandfeel/    # UI theme/look-and-feel definitions
├── kompeter-monitoring/     # Monitoring and analytics logic
├── kompeter-pointofsale/    # POS (Point of Sale) logic
├── kompeter-utilities/      # Utility functions and helpers
```

## Standard Maven Layout per Module

Each module follows the **Maven Standard Directory Layout**:

```text
module-name/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/         # Application source code
│   │   └── resources/    # Config files, assets, etc.
│   └── test/
│       ├── java/         # Unit and integration tests
│       └── resources/    # Test-specific resources
```

## Module Responsibilities

### kompeter-core

- Shared domain models (e.g., `Product`, `User`, `Transaction`).
- Business rules reusable across modules.
- Core exceptions and interfaces.

Example Classes

- `Product`
- `Sale`
- `User`
- `Discount`
- `Constants`

### kompeter-database

- DAO classes for data persistence.
- Directly talks to the database (e.g., JDBC/Hibernate/JPA).
- Does **not** contain business logic, only CRUD operations.

Example Classes

- `ProductEntity`
- `ProductRepository`
- `SaleRepository`
- `SaleEntity`
- `UserRepository`
- `UserEntity`

### kompeter-pointofsale

- Sales logic: checkout, payments, receipts.
- Customer session management.
- Deducts stock via `inventory`.
- Stores sales into `database`.

### kompeter-inventory

- Stock management: items, categories, suppliers.
- Services for adding, updating, and removing items.
- Persists inventory into `database`.
- Provides APIs for `pointofsale` and `monitoring`.

### kompeter-monitoring

- Business logic for **dashboards and analytics**.
- Collects data from `pointofsale` and `inventory`.
- Generates sales reports, stock movement summaries, KPIs.
- Used by the **desktop app** to present monitoring dashboards.

## Flow of Dependencies

```text
          +----------------+
          |  App Desktop   |
          +----------------+
            /     |      \
           /      |       \
          v       v        v
   +-----------+  +-----------+  +-----------+
   | POS       |  | Inventory |  | Monitoring|
   +-----------+  +-----------+  +-----------+
           |          |              |
           v          v              |
        +-----------------------------+
        |          Database            |
        +-----------------------------+
```

- **App Desktop** calls into `pointofsale`, `inventory`, and `monitoring` (all are business logic modules).
- **Point of Sale** depends on `inventory` (to update stock) and `database` (to store sales).
- **Inventory** manages stock and persists changes into `database`.
- **Monitoring** queries both `pointofsale` and `inventory` data, and is also persisted in `database`.
