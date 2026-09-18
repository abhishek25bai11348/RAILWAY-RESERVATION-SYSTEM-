import java.util.EnumMap;
import java.util.Map;

public class Train {
    private final String trainNumber;
    private final String trainName;
    private final String source;
    private final String destination;
    private final String departureTime;
    private final String arrivalTime;
    private final double baseFare;
    private final Map<SeatClass, Integer> capacityByClass;

    public Train(String trainNumber, String trainName, String source, String destination,
                 String departureTime, String arrivalTime, double baseFare,
                 int sleeperCapacity, int ac3Capacity, int ac2Capacity, int firstClassCapacity) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.baseFare = baseFare;
        this.capacityByClass = new EnumMap<>(SeatClass.class);
        capacityByClass.put(SeatClass.SLEEPER, sleeperCapacity);
        capacityByClass.put(SeatClass.AC_THREE_TIER, ac3Capacity);
        capacityByClass.put(SeatClass.AC_TWO_TIER, ac2Capacity);
        capacityByClass.put(SeatClass.FIRST_CLASS, firstClassCapacity);
    }

    public String getTrainNumber() { return trainNumber; }
    public String getTrainName() { return trainName; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public double getBaseFare() { return baseFare; }

    public int getCapacity(SeatClass seatClass) {
        return capacityByClass.get(seatClass);
    }

    public String toCsv() {
        return String.join(",", trainNumber, trainName, source, destination,
                departureTime, arrivalTime, String.valueOf(baseFare),
                String.valueOf(getCapacity(SeatClass.SLEEPER)),
                String.valueOf(getCapacity(SeatClass.AC_THREE_TIER)),
                String.valueOf(getCapacity(SeatClass.AC_TWO_TIER)),
                String.valueOf(getCapacity(SeatClass.FIRST_CLASS)));
    }

    public static Train fromCsv(String line) {
        String[] p = line.split(",", -1);
        if (p.length != 11) throw new IllegalArgumentException("Invalid train record.");
        return new Train(p[0], p[1], p[2], p[3], p[4], p[5], Double.parseDouble(p[6]),
                Integer.parseInt(p[7]), Integer.parseInt(p[8]), Integer.parseInt(p[9]), Integer.parseInt(p[10]));
    }

    @Override
    public String toString() {
        return trainNumber + " | " + trainName + " | " + source + " -> " + destination
                + " | Dep: " + departureTime + " | Arr: " + arrivalTime;
    }
}
