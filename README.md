***

## 🛑 CSD214 S26 Lecture 5:  Introduction to JDBC, Raw SQL and in class exercise
This repository serves as the official base for the **Lecture 5 In-Class Exercise**. Building upon the Object-Oriented foundations established in previous weeks, we are now transitioning from volatile, in-memory storage (ArrayLists) to persistent storage using **MySQL** and **JDBC (Java Database Connectivity)**.

### **JDBC Implementation Overview**
The `bookstore.jdbc` package contains new logic that demonstrates the lifecycle of data within a professional Java application:

*   **Database Infrastructure:** Utilizes the provided `docker-compose.yml` to spin up a containerized MySQL 8.0 instance.
*   **CRUD Operations:** `JdbcBookApp.java` provides a blueprint for the **Create, Read, Update, and Delete** cycle using `PreparedStatement` to ensure protection against SQL Injection attacks.
*   **Object-Relational Bridging:** `JdbcFullApp.java` demonstrates how to "rehydrate" Java objects from database rows, execute business logic (such as `sellItem()`), and synchronize those changes back to the database.
*   **Dependency Management:** The `pom.xml` has been updated with the necessary MySQL Connector/J drivers to facilitate communication between the JVM and the database container.

**Goal:** By the end of this exercise, you will understand how to replace a temporary `ArrayList` with a robust, persistent database while maintaining the integrity of your existing class hierarchy.

***
***

# Bookstore CLI Application
- for the csd214 course summer 26 delivery : [course outline](https://welearn.saultcollege.ca/shared/CourseOutlines/csd214_Course_Outline.pdf) 
> - [git repository](https://github.com/fcarella/lab1-exercise-fred-carella-csd214-s26)
> - [based on bookstore (git repository): ](https://github.com/fcarella/bookstore-2026-01-30)
> - [this exercise is covered in this lecture...](https://docs.google.com/document/d/1N8mfXnbmEHYcpEE1G3YnafFOytEmAnNpJvF6KsBTht0/edit?usp=sharing)
- A console-based Java application for managing a bookstore inventory, performing sales, and tracking cash flow. This project demonstrates object-oriented programming concepts including inheritance, polymorphism, and interface implementation in Java 24.

## Features

*   **Inventory Management:**
    *   **Books:** Manage items with Title, Author, Price, and Copies.
    *   **Magazines:** Manage periodicals with Order Quantity and Issue Date.
    *   **Disc Magazines:** Specialized magazines that include a disc.
    *   **Tickets:** Simple saleable items with a description and price.
    *   **Vehicle Parts (Lab 1 Exercise):** Expanded inventory to include **Tires** (Diameter) and **Batteries** (Cold Cranking Amps) via a new Automotive hierarchy.
*   **CRUD Operations:** Add, Edit, and Delete items from the inventory.
*   **Sales System:** Sell items to decrement inventory count and increase the Cash Till total.
*   **Data Generation:** Uses `JavaFaker` to populate the inventory with realistic dummy data for both Bookstore and Auto-Shop departments.
*   **Menu System:** Interactive console menu for navigation.

## Class Hierarchy

![Class Diagram](documentation/bookstore-2026-01-30-142617.png)

The hierarchy implements the following structure:
*   **SaleableItem (Interface):** Defines `sellItem()` and `getPrice()`.
*   **Editable (Abstract):** Handles console input/output and parsing.
*   **Publication:** Base class for Books and Magazines (Title, Price, Copies).
*   **VehiclePart (Lab 1 Exercise):** Base abstract class for the automotive department (Manufacturer, Price).
    *   `Tire` and `Battery` concrete classes implement specific automotive attributes.

## Prerequisites

*   **Java JDK:** Version 24
*   **Maven:** 3.6+

## Dependencies

*   [JavaFaker](https://github.com/DiUS/java-faker) (1.0.2): For generating random test data.
*   [JUnit 5](https://junit.org/junit5/) (5.10.0): For unit testing.

## How to Run

1.  **Compile the project:**
    ```bash
    mvn clean compile
    ```

2.  **Run the application:**
    ```bash
    mvn exec:java -Dexec.mainClass="bookstore.Main"
    ```

## Usage

Upon starting, the application will populate the list with random data. You will see the following menu:

```text
***********************
 1. Add Items
 2. Edit Items
 3. Delete Items
 4. Sell item(s)
 5. List items
99. Quit
***********************
```

*   **Add Items:** Choose a specific type (Book, Magazine, Tire, Battery, etc.) and follow the prompts.
*   **Edit Items:** Select an index from the list to modify fields.
*   **Sell Items:** Select an index to sell. This decreases the 'Copies' count (for Publications) and adds the price to the internal Cash Till.

## Running Tests

Unit tests are implemented using JUnit 5 to verify the logic of POJOs and input mocking.

Run the tests using Maven:

```bash
mvn test
```

## Project Structure

```
src/
├── main/
│   └── java/
│       └── bookstore/
│           ├── Main.java           # Entry point
│           ├── App.java            # Controller / Menu Logic
│           └── pojos/              # Data Models
│               ├── Editable.java
│               ├── SaleableItem.java
│               ├── Product.java
│               ├── Publication.java
│               ├── Book.java
│               ├── Magazine.java
│               ├── DiscMag.java
│               ├── Ticket.java
│               ├── VehiclePart.java    # New Lab 1 Parent
│               ├── Tire.java           # New Lab 1 Child
│               ├── Battery.java        # New Lab 1 Child
│               └── CashTill.java
└── test/
    └── java/
        └── bookstore/
            └── pojos/              # Unit Tests
```