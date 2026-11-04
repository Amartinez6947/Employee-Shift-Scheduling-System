# Employee Shift Scheduling and Availability Management System

## Project Overview

The Employee Shift Scheduling and Availability Management System is a web-based application designed to help small businesses manage employee information, track availability, and organize work schedules.

The system provides a centralized way to manage employee records, record availability, create and update shifts, and view weekly schedules. It also includes scheduling validation to help prevent conflicts and ensure employees are assigned shifts that match their availability.

## Technologies Used

- **Java 21** – Primary programming language
- **Spring Boot 4.1.2** – Application framework
- **Spring Data JPA** – Database access and persistence
- **Thymeleaf** – Server-side HTML templates
- **HTML and CSS** – User interface
- **SQLite** – Database storage
- **Maven** – Project build and dependency management
- **JUnit and Mockito** – Application testing
- **Android Studio** – Development environment

## Application Features

### Employee Management
- Add employee records.
- View employee information.
- Update existing employee records.

### Employee Availability
- Record employee availability by day of the week.
- Specify availability start and end times.
- Validate availability time ranges.
- View saved employee availability.

### Shift Management
- Create employee shifts.
- View scheduled shifts.
- Edit existing shifts.
- Delete shifts.
- Validate employee assignments and shift times.

### Scheduling and Conflict Detection
- Check employee availability before assigning shifts.
- Detect overlapping shifts for the same employee.
- Allow consecutive shifts when they do not overlap.
- Display a weekly schedule from Monday through Sunday.
- Navigate between previous and upcoming weeks.

### Data Persistence
- Store employee, availability, and shift information in a SQLite database.
- Preserve saved records when the application is restarted.

## Project Structure

```text
shift-scheduler/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/com/example/shiftscheduler/
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── ShiftSchedulerApplication.java
│   │   └── resources/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/shiftscheduler/
│           └── ShiftSchedulerApplicationTests.java
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml
```

## Requirements

To run the application locally, you will need:

- Java Development Kit (JDK) 21
- GitHub project files
- An internet connection for Maven to download required dependencies the first time the project is built.

Maven is provided through the included Maven Wrapper.

## Running the Application

1. Download or clone the repository.
2. Open the project in your development environment.
3. Make sure JDK 21 is configured.
4. Open a terminal in the project root directory.
5. Run the following command on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

6. Once the application starts, open the following address in your browser:

```text
http://localhost:8080
```

The application uses SQLite for local database storage. The database is configured to use:

```text
./data/shift_scheduler.db
```

The application creates the database directory and required tables when it starts.

## Running Tests

To execute the automated test suite, open a terminal in the project root and run:

```powershell
.\mvnw.cmd test
```

The test suite includes checks for application functionality and scheduling validation, including employee availability and shift conflict detection.

## Project Testing

The application was manually tested to verify employee management, availability validation, shift creation and editing, conflict detection, weekly schedule navigation, shift deletion, and database persistence.

The automated test suite also includes 11 tests covering service-layer behavior. The latest recorded test run completed successfully with zero failures and zero errors.

## Project Purpose

This project demonstrates the application of software engineering concepts, object-oriented design, database integration, application development, testing, and software configuration management.

It was developed as part of the CSC480 Capstone Project.
