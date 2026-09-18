import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;

public class RailwayReservationSystem {
    private final List<Train> trains;
    private final List<Reservation> reservations;
    private final FileManager fileManager;
    private final FareCalculator fareCalculator;
    private final Scanner scanner;

    public RailwayReservationSystem(FileManager fileManager) throws IOException {
        this.fileManager = fileManager;
        this.fileManager.initialize();
        this.trains = fileManager.loadTrains();
        this.reservations = fileManager.loadReservations();
        this.fareCalculator = new StandardFareCalculator();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        printHeader();
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": displayAllTrains(); break;
                    case "2": searchTrains(); break;
                    case "3": bookTicket(); break;
                    case "4": cancelTicket(); break;
                    case "5": viewTicket(); break;
                    case "6": viewAllReservations(); break;
                    case "7": checkAvailability(); break;
                    case "8": printStatistics(); break;
                    case "0":
                        System.out.println("\nThank you for using Railway Reservation System.");
                        return;
                    default: System.out.println("Invalid choice. Please enter a valid menu number.");
                }
            } catch (ValidationException | IOException | IllegalArgumentException ex) {
                System.out.println("Operation failed: " + ex.getMessage());
            }
        }
    }

    private void printHeader() {
        System.out.println("\n============================================================");
        System.out.println("             RAILWAY RESERVATION SYSTEM");
        System.out.println("============================================================");
    }

    private void printMenu() {
        System.out.println("\n---------------------- MAIN MENU ---------------------------");
        System.out.println("1. Display all trains");
        System.out.println("2. Search trains by route");
        System.out.println("3. Book a ticket");
        System.out.println("4. Cancel a ticket");
        System.out.println("5. View ticket by PNR");
        System.out.println("6. View all reservations");
        System.out.println("7. Check seat availability");
        System.out.println("8. System statistics");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private void displayAllTrains() {
        System.out.println("\n---------------------- AVAILABLE TRAINS --------------------");
        for (Train train : trains) System.out.println(train);
    }

    private void searchTrains() {
        System.out.print("Enter source: ");
        String source = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
        boolean found = false;
        System.out.println("\nMatching trains:");
        for (Train train : trains) {
            if (train.getSource().toLowerCase(Locale.ROOT).equals(source)
                    && train.getDestination().toLowerCase(Locale.ROOT).equals(destination)) {
                System.out.println(train);
                found = true;
            }
        }
        if (!found) System.out.println("No trains found for the selected route.");
    }

    private void bookTicket() throws ValidationException, IOException {
        System.out.println("\n------------------------- BOOK TICKET ----------------------");
        displayAllTrains();
        System.out.print("Enter train number: ");
        String trainNumber = scanner.nextLine().trim();
        Train train = findTrain(trainNumber);
        if (train == null) throw new ValidationException("Train number not found.");

        System.out.print("Enter passenger name: ");
        String name = scanner.nextLine().trim();
        validateName(name);
        int age = readInt("Enter passenger age: ");
        if (age < 1 || age > 120) throw new ValidationException("Age must be between 1 and 120.");
        System.out.print("Enter gender: ");
        String gender = scanner.nextLine().trim();
        if (gender.isEmpty()) throw new ValidationException("Gender cannot be empty.");
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine().trim();
        if (!phone.matches("\\d{10}")) throw new ValidationException("Phone number must contain exactly 10 digits.");
        LocalDate journeyDate = readJourneyDate();

        System.out.println("\nSelect class:");
        System.out.println("1. Sleeper");
        System.out.println("2. AC 3 Tier");
        System.out.println("3. AC 2 Tier");
        System.out.println("4. First Class");
        int classOption = readInt("Enter class option: ");
        SeatClass seatClass = SeatClass.fromOption(classOption);

        int available = getAvailableSeats(train, seatClass);
        if (available <= 0) throw new ValidationException("No seats are available in the selected class.");

        int seatNumber = allocateSeat(train, seatClass);
        double fare = fareCalculator.calculateFare(train, seatClass);
        String passengerId = "P" + String.format("%05d", reservations.size() + 1);
        Passenger passenger = new Passenger(passengerId, name, age, gender, phone);
        String pnr = "PNR" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase(Locale.ROOT);

        Reservation reservation = new Reservation(pnr, passenger, train.getTrainNumber(), seatClass,
                seatNumber, fare, journeyDate, true);
        reservations.add(reservation);
        fileManager.saveReservations(reservations);

        System.out.println("\n***** TICKET BOOKED SUCCESSFULLY *****");
        System.out.println(reservation.formattedDetails(train));
        System.out.println("****************************************");
    }

    private void cancelTicket() throws IOException, ValidationException {
        System.out.print("Enter PNR to cancel: ");
        String pnr = scanner.nextLine().trim();
        Reservation reservation = findReservation(pnr);
        if (reservation == null) throw new ValidationException("PNR not found.");
        if (!reservation.isActive()) throw new ValidationException("This ticket is already cancelled.");
        reservation.cancel();
        fileManager.saveReservations(reservations);
        System.out.println("Ticket " + pnr + " cancelled successfully.");
    }

    private void viewTicket() throws ValidationException {
        System.out.print("Enter PNR: ");
        String pnr = scanner.nextLine().trim();
        Reservation reservation = findReservation(pnr);
        if (reservation == null) throw new ValidationException("PNR not found.");
        Train train = findTrain(reservation.getTrainNumber());
        System.out.println("\n------------------------- TICKET ---------------------------");
        System.out.println(reservation.formattedDetails(train));
        System.out.println("-------------------------------------------------------------");
    }

    private void viewAllReservations() {
        System.out.println("\n---------------------- RESERVATION LIST ---------------------");
        if (reservations.isEmpty()) {
            System.out.println("No reservations available.");
            return;
        }
        for (Reservation r : reservations) {
            System.out.printf("%s | %-20s | Train %s | %-13s | Seat %-3d | %s | %s%n",
                    r.getPnr(), r.getPassenger().getName(), r.getTrainNumber(),
                    r.getSeatClass().getDisplayName(), r.getSeatNumber(), r.getJourneyDate(),
                    r.isActive() ? "CONFIRMED" : "CANCELLED");
        }
    }

    private void checkAvailability() throws ValidationException {
        System.out.print("Enter train number: ");
        Train train = findTrain(scanner.nextLine().trim());
        if (train == null) throw new ValidationException("Train number not found.");
        System.out.println("\nSeat availability for " + train.getTrainName() + ":");
        for (SeatClass seatClass : SeatClass.values()) {
            System.out.println(seatClass.getDisplayName() + ": " + getAvailableSeats(train, seatClass)
                    + "/" + train.getCapacity(seatClass));
        }
    }

    private void printStatistics() {
        int confirmed = 0;
        int cancelled = 0;
        double revenue = 0;
        for (Reservation r : reservations) {
            if (r.isActive()) {
                confirmed++;
                revenue += r.getFare();
            } else cancelled++;
        }
        System.out.println("\n------------------------- STATISTICS -----------------------");
        System.out.println("Total reservations: " + reservations.size());
        System.out.println("Confirmed bookings: " + confirmed);
        System.out.println("Cancelled bookings: " + cancelled);
        System.out.printf("Confirmed booking revenue: Rs. %.2f%n", revenue);
    }

    private Train findTrain(String trainNumber) {
        for (Train train : trains) {
            if (train.getTrainNumber().equalsIgnoreCase(trainNumber)) return train;
        }
        return null;
    }

    private Reservation findReservation(String pnr) {
        for (Reservation reservation : reservations) {
            if (reservation.getPnr().equalsIgnoreCase(pnr)) return reservation;
        }
        return null;
    }

    private int getAvailableSeats(Train train, SeatClass seatClass) {
        int booked = 0;
        for (Reservation r : reservations) {
            if (r.isActive() && r.getTrainNumber().equals(train.getTrainNumber()) && r.getSeatClass() == seatClass) {
                booked++;
            }
        }
        return train.getCapacity(seatClass) - booked;
    }

    private int allocateSeat(Train train, SeatClass seatClass) {
        int capacity = train.getCapacity(seatClass);
        boolean[] occupied = new boolean[capacity + 1];
        for (Reservation r : reservations) {
            if (r.isActive() && r.getTrainNumber().equals(train.getTrainNumber()) && r.getSeatClass() == seatClass) {
                if (r.getSeatNumber() <= capacity) occupied[r.getSeatNumber()] = true;
            }
        }
        for (int i = 1; i <= capacity; i++) if (!occupied[i]) return i;
        throw new IllegalStateException("No seat is available.");
    }

    private int readInt(String prompt) throws ValidationException {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Please enter a valid integer.");
        }
    }

    private LocalDate readJourneyDate() throws ValidationException {
        System.out.print("Enter journey date (YYYY-MM-DD): ");
        try {
            LocalDate date = LocalDate.parse(scanner.nextLine().trim());
            if (date.isBefore(LocalDate.now())) throw new ValidationException("Journey date cannot be in the past.");
            return date;
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid date. Use YYYY-MM-DD format.");
        }
    }

    private void validateName(String name) throws ValidationException {
        if (name.isEmpty() || name.length() < 2) throw new ValidationException("Name must contain at least 2 characters.");
        if (!name.matches("[A-Za-z .'-]+")) throw new ValidationException("Name contains invalid characters.");
    }
}
