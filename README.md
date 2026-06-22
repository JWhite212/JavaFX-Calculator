# JavaFX Calculator

A desktop calculator application built with JavaFX, following the MVC (Model-View-Controller) pattern. Supports four binary operations (addition, subtraction, multiplication, division) and two unary operations (square root, negation).

Built as a university assignment (University of Kent, CO871 module) to demonstrate object-oriented design, event-driven GUI programming, and separation of concerns.

![Image of Calculator](/Calculator.png)

## Features

- Four binary operations: addition, subtraction, multiplication, division
- Two unary operations: square root, negation
- Running formula display showing the current expression
- Chained calculations (pressing an operator after a result carries it forward)
- Error handling for division by zero and square root of negative numbers
- Decimal point input support
- Clear and delete (backspace) functions
- Negative operands displayed in parentheses for readability

## Prerequisites

- Java 17 or later
- Maven 3.8+

## Build and Run

```bash
# Build the project
mvn clean compile

# Run the application
mvn clean javafx:run

# Run tests (JUnit 5)
mvn test

# Package as a JAR
mvn clean package
```

## Project Structure

```
JavaFX-Calculator/
  pom.xml                                          # Maven build configuration
  Calculator.png                                   # Screenshot
  src/main/java/
    module-info.java                               # JPMS module descriptor
    jamie/javafx/calculator/
      Main.java                                    # Application entry point
      Controller.java                              # FXML controller (UI event handling)
      Model.java                                   # Calculator logic (operations, state)
  src/main/resources/
    jamie/javafx/calculator/
      GUI.fxml                                     # UI layout definition
```

## Design

The project uses an MVC architecture:

- **Model** (`Model.java`): Pure business logic with no JavaFX dependencies. Operations are stored in immutable maps dispatched by string key. Uses `OptionalDouble` for nullable numeric state.
- **View** (`GUI.fxml`): Declarative FXML layout defining the calculator UI. No inline logic.
- **Controller** (`Controller.java`): Bridges Model and View via JavaFX `StringProperty` bindings. All FXML-annotated handler methods delegate arithmetic to the Model.
