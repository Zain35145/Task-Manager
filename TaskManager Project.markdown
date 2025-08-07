# TaskManager: A Java-Based Task Scheduling System

## Overview
Welcome to **TaskManager**, a lightweight Java application for managing and scheduling tasks with dependencies. This project is designed to showcase my skills in writing comprehensive unit test suites for complex codebases, as well as my ability to work with Java, JUnit 5, and Docker. It’s a simplified take on task management systems inspired by tools like Apache Maven, but built from scratch to demonstrate my coding and testing capabilities.

The project includes:
- A core task management system with task creation, dependency resolution, and scheduling logic.
- An extensive unit test suite using JUnit 5, covering edge cases, error handling, and core functionality.
- Docker support for containerized development and testing.
- A humanized codebase with clear comments and documentation.

This project was created as part of my application to Mercor’s Software Engineer role, focusing on writing and reviewing unit tests for complex open-source projects.

## Features
- **Task Creation**: Define tasks with unique IDs, names, and execution times.
- **Dependency Management**: Specify task dependencies and resolve them in the correct order.
- **Scheduling**: Execute tasks in a valid order based on dependencies, handling cyclic dependencies and errors.
- **Comprehensive Unit Tests**: Extensive JUnit 5 tests covering task creation, dependency resolution, and scheduling logic.
- **Docker Support**: Run the application and tests in a containerized environment.

## Getting Started

### Prerequisites
- **Java 17** or later
- **Maven 3.8+** for building and dependency management
- **Docker** (optional, for containerized execution)
- **Git** for cloning the repository

### Installation
1. **Clone the Repository**
   ```bash
   git clone <your-repo-url>
   cd taskmanager
   ```

2. **Build the Project**
   Use Maven to compile and run tests:
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   Execute the main class to see a sample task scheduling demo:
   ```bash
   mvn exec:java -Dexec.mainClass="com.taskmanager.Main"
   ```

4. **Run Tests**
   Execute the unit tests to verify the codebase:
   ```bash
   mvn test
   ```

5. **Run with Docker**
   Build and run the application in a Docker container:
   ```bash
   docker build -t taskmanager .
   docker run taskmanager
   ```

## Project Structure
```
taskmanager/
├── src/
│   ├── main/
│   │   └── java/com/taskmanager/
│   │       ├── Task.java           # Task model with ID, name, and execution time
│   │       ├── TaskManager.java    # Core logic for task management and scheduling
│   │       └── Main.java           # Entry point with a sample demo
│   ├── test/
│   │   └── java/com/taskmanager/
│   │       └── TaskManagerTest.java # Comprehensive unit test suite
├── Dockerfile                      # Docker configuration for containerized execution
├── pom.xml                         # Maven configuration with dependencies
└── README.md                       # This file
```

## Usage
The `Main` class provides a simple demo that creates tasks, adds dependencies, and schedules them. Here’s a quick example of how to use the TaskManager programmatically:

```java
TaskManager manager = new TaskManager();
manager.addTask(new Task("1", "Compile Code", 5));
manager.addTask(new Task("2", "Run Tests", 3));
manager.addDependency("2", "1"); // Tests depend on compilation
List<Task> schedule = manager.scheduleTasks();
System.out.println("Execution order: " + schedule);
```

For detailed testing, check out the `TaskManagerTest` class, which includes over 20 test cases covering task creation, dependency validation, scheduling, and error handling.

## Unit Testing
The project emphasizes rigorous unit testing with JUnit 5. The `TaskManagerTest` class includes:
- Tests for task creation and validation (e.g., duplicate IDs, invalid execution times).
- Tests for dependency management (e.g., valid dependencies, cyclic dependencies).
- Tests for scheduling logic (e.g., correct order, handling of disconnected tasks).
- Edge cases like empty task lists, self-dependencies, and large dependency graphs.

To run the tests:
```bash
mvn test
```

The test suite achieves high code coverage (targeting >80%) and includes descriptive test names and assertions for clarity.

## Docker Setup
The `Dockerfile` sets up a containerized environment for running the application and tests. It uses a Java 17 base image and Maven for building. To build and run:
```bash
docker build -t taskmanager .
docker run taskmanager
```

The Docker container executes the test suite by default, but you can modify the `CMD` to run the main application instead.

## Contributing
Contributions are welcome! If you’d like to contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Add your changes and write corresponding unit tests.
4. Submit a pull request with a clear description of your changes.

Please ensure all tests pass (`mvn test`) and follow standard Java coding conventions.

## Why This Project?
I built TaskManager to demonstrate my ability to:
- Write clean, maintainable Java code for a moderately complex system.
- Create a comprehensive unit test suite that covers core functionality and edge cases.
- Work with Docker for containerized development.
- Document my work in a clear, humanized way (like this README!).

This project reflects my experience contributing to open-source projects and my focus on writing high-quality, testable code. I’ve aimed to make the codebase approachable yet complex enough to showcase my skills in unit testing and dependency management.

## Contact
Feel free to reach out via GitHub issues or my GitHub profile (linked in my application) for questions or feedback. I’m excited to discuss my contributions in detail during the interview process!

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.