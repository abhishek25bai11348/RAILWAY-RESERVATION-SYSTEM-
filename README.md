# Railway Reservation System - Java Flipped Course Project

## 1. Project Overview

The **Railway Reservation System** is a menu-driven Java console application designed to demonstrate core Java programming and object-oriented programming concepts in a practical real-world scenario.

The system allows a user to search trains, check seat availability, book tickets, view booking details, cancel tickets, and view reservation statistics. Reservation records are stored in CSV files so that information remains available after the program is closed.

## 2. Objectives

- Apply Java classes and objects to a real-world problem.
- Demonstrate encapsulation, inheritance, abstraction, and interfaces.
- Use Java Collections for in-memory data management.
- Use exception handling and input validation.
- Implement file handling for persistent storage.
- Build a modular and maintainable console application.

## 3. Key Features

1. Display available trains.
2. Search trains by source and destination.
3. Book a railway ticket.
4. Automatically allocate the next available seat.
5. Calculate fare according to class.
6. Generate a unique PNR number.
7. View a ticket using PNR.
8. Cancel an active ticket.
9. Check class-wise seat availability.
10. Display reservation statistics.
11. Store trains and reservations in CSV files.
12. Validate passenger details and handle invalid inputs.

## 4. Java Concepts Used

- Classes and Objects
- Constructors
- Encapsulation
- Inheritance (`Passenger` extends `Person`)
- Abstraction (`Person` is abstract)
- Interface (`FareCalculator`)
- Polymorphism through interface-based fare calculation
- Enum (`SeatClass`)
- Collections (`List`, `ArrayList`)
- File Handling (`java.nio.file`)
- Exception Handling
- String processing
- Date handling with `LocalDate`
- Loops and conditional statements
- Methods and modular programming

## 5. Project Structure

```text
RailwayReservationSystem/
├── src/
│   ├── Main.java
│   ├── Person.java
│   ├── Passenger.java
│   ├── SeatClass.java
│   ├── Train.java
│   ├── FareCalculator.java
│   ├── StandardFareCalculator.java
│   ├── Reservation.java
│   ├── ValidationException.java
│   ├── FileManager.java
│   └── RailwayReservationSystem.java
├── data/
│   ├── trains.csv
│   └── reservations.csv
├── docs/
└── README.md
```

## 6. Requirements

- Java Development Kit (JDK) 8 or above.
- Command Prompt/Terminal or any Java IDE such as IntelliJ IDEA, Eclipse, NetBeans, or VS Code.

No external Java libraries are required.

## 7. How to Run

### Using Command Prompt / Terminal

Open a terminal in the project folder and run:

```bash
cd src
javac *.java
java Main
```

The application creates/uses the `data` directory relative to the `src` execution directory. Therefore, for the exact commands above, run the program from the `src` directory. The project package is intentionally omitted to keep setup simple for a flipped-course demonstration.

### Recommended run from project root

From the project root, compile into a separate output directory:

```bash
mkdir -p out
javac -d out src/*.java
java -cp out Main
```

On Windows PowerShell/CMD, the same Java commands work after creating the `out` directory:

```text
mkdir out
javac -d out src\*.java
java -cp out Main
```

In this mode, the application will create the `data` folder in the project root.

## 8. Main Menu

```text
1. Display all trains
2. Search trains by route
3. Book a ticket
4. Cancel a ticket
5. View ticket by PNR
6. View all reservations
7. Check seat availability
8. System statistics
0. Exit
```

## 9. Sample Booking Flow

1. Select **3 - Book a ticket**.
2. Enter a valid train number, for example `12001`.
3. Enter passenger details.
4. Enter a future journey date in `YYYY-MM-DD` format.
5. Select the travel class.
6. The system checks availability and allocates a seat.
7. Fare is calculated automatically.
8. A unique PNR is displayed.
9. Reservation data is stored in `data/reservations.csv`.

## 10. Data Files

### `data/trains.csv`
Contains sample train information, route details, timings, base fare, and class capacities.

### `data/reservations.csv`
Contains passenger and booking records, including PNR, train number, seat class, seat number, fare, journey date, and status.

Do not edit `reservations.csv` manually while the application is running.

## 11. Important Design Notes

- The application is intentionally console based so that Java fundamentals remain the focus.
- Fare calculation is separated behind the `FareCalculator` interface, allowing another fare strategy to be added later.
- Cancelled reservations remain stored but are marked as `CANCELLED`, preserving transaction history.
- Active reservations are used to calculate current seat availability.

## 12. Future Enhancements

- GUI using Java Swing or JavaFX.
- Database integration using JDBC and MySQL.
- User login and administrator roles.
- Waitlist and RAC management.
- Multiple passengers in one booking.
- PDF ticket generation.
- Email/SMS notifications.
- Admin interface for adding and updating trains.

## 13. Academic Use

This project is suitable as a Java flipped-course/project-exhibition demonstration because it connects programming concepts to a familiar real-world system while keeping the implementation understandable for a student-level viva.
