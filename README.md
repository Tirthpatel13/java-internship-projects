# Java Internship Projects

Two Java console applications built for the internship tasks.

| Task | Project |
|---|---|
| 1 | [Student Grade Tracker](task1-grade-tracker) |
| 2 | [Stock Trading Platform](task2-stock-trading) |

## Task 1: Student Grade Tracker
- Input and manage student grades
- Calculate average, highest and lowest scores
- Store data with `ArrayList`
- Display a summary report of all students
- Console-based interface (optional Swing GUI)

## Task 2: Stock Trading Platform
- Simulated stock market with live-changing prices
- Market data display and buy/sell operations
- Portfolio performance tracking over time
- Object-oriented design: stocks, users, portfolios, transactions
- File I/O to persist portfolio data

## Requirements
- Java 21+
- Maven 3.9+

## Build and run
```bash
mvn package
java -jar task1-grade-tracker/target/task1-grade-tracker-1.0-SNAPSHOT.jar
java -jar task2-stock-trading/target/task2-stock-trading-1.0-SNAPSHOT.jar
```

## Project structure
```
java-internship-projects/
├── pom.xml                 # parent Maven build
├── task1-grade-tracker/    # Task 1 source and tests
└── task2-stock-trading/    # Task 2 source and tests
```
