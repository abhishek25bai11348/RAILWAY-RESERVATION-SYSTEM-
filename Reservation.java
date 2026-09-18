import java.time.LocalDate;

public class Reservation {
    private final String pnr;
    private final Passenger passenger;
    private final String trainNumber;
    private final SeatClass seatClass;
    private final int seatNumber;
    private final double fare;
    private final LocalDate journeyDate;
    private boolean active;

    public Reservation(String pnr, Passenger passenger, String trainNumber, SeatClass seatClass,
                       int seatNumber, double fare, LocalDate journeyDate, boolean active) {
        this.pnr = pnr;
        this.passenger = passenger;
        this.trainNumber = trainNumber;
        this.seatClass = seatClass;
        this.seatNumber = seatNumber;
        this.fare = fare;
        this.journeyDate = journeyDate;
        this.active = active;
    }

    public String getPnr() { return pnr; }
    public Passenger getPassenger() { return passenger; }
    public String getTrainNumber() { return trainNumber; }
    public SeatClass getSeatClass() { return seatClass; }
    public int getSeatNumber() { return seatNumber; }
    public double getFare() { return fare; }
    public LocalDate getJourneyDate() { return journeyDate; }
    public boolean isActive() { return active; }
    public void cancel() { active = false; }

    public String toCsv() {
        return String.join(",", pnr, passenger.getPassengerId(), safe(passenger.getName()),
                String.valueOf(passenger.getAge()), safe(passenger.getGender()), safe(passenger.getPhone()),
                trainNumber, seatClass.name(), String.valueOf(seatNumber), String.valueOf(fare),
                journeyDate.toString(), String.valueOf(active));
    }

    private String safe(String value) {
        return value.replace(",", " ");
    }

    public static Reservation fromCsv(String line) {
        String[] p = line.split(",", -1);
        if (p.length != 12) throw new IllegalArgumentException("Invalid reservation record.");
        Passenger passenger = new Passenger(p[1], p[2], Integer.parseInt(p[3]), p[4], p[5]);
        return new Reservation(p[0], passenger, p[6], SeatClass.valueOf(p[7]), Integer.parseInt(p[8]),
                Double.parseDouble(p[9]), LocalDate.parse(p[10]), Boolean.parseBoolean(p[11]));
    }

    public String formattedDetails(Train train) {
        return "PNR: " + pnr + "\n"
                + "Passenger: " + passenger.getName() + " (Age " + passenger.getAge() + ", " + passenger.getGender() + ")\n"
                + "Phone: " + passenger.getPhone() + "\n"
                + "Train: " + train.getTrainNumber() + " - " + train.getTrainName() + "\n"
                + "Route: " + train.getSource() + " -> " + train.getDestination() + "\n"
                + "Journey Date: " + journeyDate + "\n"
                + "Class: " + seatClass.getDisplayName() + "\n"
                + "Seat: " + seatNumber + "\n"
                + "Fare: Rs. " + String.format("%.2f", fare) + "\n"
                + "Status: " + (active ? "CONFIRMED" : "CANCELLED");
    }
}
