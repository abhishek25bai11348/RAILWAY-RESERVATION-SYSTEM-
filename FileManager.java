import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private final Path dataDirectory;
    private final Path trainsFile;
    private final Path reservationsFile;

    public FileManager(String directory) {
        dataDirectory = Paths.get(directory);
        trainsFile = dataDirectory.resolve("trains.csv");
        reservationsFile = dataDirectory.resolve("reservations.csv");
    }

    public void initialize() throws IOException {
        Files.createDirectories(dataDirectory);
        if (!Files.exists(trainsFile)) {
            List<String> lines = new ArrayList<>();
            lines.add("trainNumber,trainName,source,destination,departureTime,arrivalTime,baseFare,sleeperCapacity,ac3Capacity,ac2Capacity,firstClassCapacity");
            lines.add("12001,Jan Shatabdi Express,Bhopal,New Delhi,06:00,14:30,850,80,40,30,10");
            lines.add("12002,Bhopal Intercity,Bhopal,Indore,07:15,12:20,520,100,50,25,5");
            lines.add("12155,Rani Kamlapati Express,Bhopal,Mumbai,16:10,08:45,1250,120,60,30,10");
            lines.add("12628,Karnataka Express,New Delhi,Bengaluru,20:30,05:55,1600,150,70,35,10");
            lines.add("12920,Malwa Superfast,Indore,New Delhi,17:40,07:25,1450,140,60,30,10");
            Files.write(trainsFile, lines);
        }
        if (!Files.exists(reservationsFile)) {
            Files.write(reservationsFile, java.util.Collections.singletonList(
                    "pnr,passengerId,name,age,gender,phone,trainNumber,seatClass,seatNumber,fare,journeyDate,active"));
        }
    }

    public List<Train> loadTrains() throws IOException {
        List<Train> trains = new ArrayList<>();
        List<String> lines = Files.readAllLines(trainsFile);
        for (int i = 1; i < lines.size(); i++) {
            if (!lines.get(i).trim().isEmpty()) trains.add(Train.fromCsv(lines.get(i)));
        }
        return trains;
    }

    public List<Reservation> loadReservations() throws IOException {
        List<Reservation> reservations = new ArrayList<>();
        List<String> lines = Files.readAllLines(reservationsFile);
        for (int i = 1; i < lines.size(); i++) {
            if (!lines.get(i).trim().isEmpty()) reservations.add(Reservation.fromCsv(lines.get(i)));
        }
        return reservations;
    }

    public void saveReservations(List<Reservation> reservations) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("pnr,passengerId,name,age,gender,phone,trainNumber,seatClass,seatNumber,fare,journeyDate,active");
        for (Reservation reservation : reservations) lines.add(reservation.toCsv());
        Files.write(reservationsFile, lines);
    }
}
