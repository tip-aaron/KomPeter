# Testing Guide

This project uses **JUnit 5 (Jupiter)** for testing.

## Running Tests

Run all tests across modules:
mvn test

Run tests for a specific module:
mvn -pl kompeter-core test

### Writing Tests

1. Create test classes under `src/test/java`.
2. Follow naming convention: `ClassNameTest.java`.
3. Use JUnit 5 annotations:
   - @Test → Marks a test method.
   - @BeforeEach → Runs before each test.
   - @AfterEach → Runs after each test.
   - @ParameterizedTest → Runs the same test with multiple inputs.

Example:

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void testAddition() {
        int result = 2 + 3;
        assertEquals(5, result);
    }
}

### Test Reports

After running `mvn test`, reports are generated in:
module-name/target/surefire-reports/

### Tips

- Keep tests modular: test each module’s functionality in its own test package.
- Use `mvn clean test` to ensure no old build artifacts affect results.
- For integration tests, consider placing them under `src/test/java` with clear naming, or configure `maven-failsafe-plugin` later.
-
