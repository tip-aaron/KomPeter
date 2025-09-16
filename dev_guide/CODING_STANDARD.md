# Coding Standard

This guide defines the coding standards for our project.

## 1. Formatting

- Indentation: 4 spaces (no tabs).
- Braces: Use K&R style

  ```java
  if (condition) {
      // code
  } else {
      // code
  }
  ```

## 2. Naming Conventions

- Classes & Interfaces: `PascalCase`
    Example: `UserService`, `InventoryController`
- Methods & Variables: `camelCase`
    Example: `getUser()`, `itemCount`
- Constants: `UPPER_SNAKE_CASE`
    Example: `MAX_USERS`, `DEFAULT_TIMEOUT`
- Packages: all lowercase
    Example: `com.github.ragudos.kompeter.inventory.tracker`

## 3. Comments and Documentation

- Use *Javadoc* for public classes and methods

    ```java
        /**
        * Calculates the total sales.
        * @param items list of sold items
        * @return total sales amount
        */
        public double calculateSales(List<Item> items) { ... }
    ```

## 4. Best Practices

- No `System.out.println` - Use `KompeterLogger`

    ```java
        public class InventoryRepository {
            protected static final KompeterLogger LOGGER =
                KompeterLogger.getLogger(InventoryRepository.class);
        }
    ```

- Handle exceptions properly (avoid empty `catch`). Default handling should be printing the error

    ```java
        void doSomething() {
            try {
                // some code
            } catch (Error err) {
                LOGGER.log(Level.SEVERE, "Something went wrong", err);
            }
        }
    ```

- Prefer using `Optional` over `null`
    This will prevent unintentional `NullPointerException` errors, and allow us to handle `empty` or `null` values more explicitly.

    ```java
        Optional<String> findString(String id) {
            for (SomeRecord record : recordCollection) {
                if (record.id.equals(id)) {
                    return Optional.of(record.value);
                }
            }

            return Optional.empty();
        }
    ```
