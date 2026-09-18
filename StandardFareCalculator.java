public class StandardFareCalculator implements FareCalculator {
    @Override
    public double calculateFare(Train train, SeatClass seatClass) {
        double fare = train.getBaseFare() * seatClass.getMultiplier();
        double reservationCharge = 20.0;
        double serviceCharge = fare * 0.02;
        return Math.round((fare + reservationCharge + serviceCharge) * 100.0) / 100.0;
    }
}
